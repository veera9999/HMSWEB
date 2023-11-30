// Sample data for available timeslots (for demonstration purposes)
const availableTimeslots = [
    '09:00 AM', '09:30 AM', '10:00 AM', '10:30 AM',
    '11:00 AM', '11:30 AM', '01:00 PM', '01:30 PM',
    '02:00 PM', '02:30 PM', '03:00 PM', '03:30 PM',
    '04:00 PM', '04:30 PM', '05:00 PM', '05:30 PM',
];

let selectedTimeslot = null;
let doctorsList = null;
document.addEventListener('DOMContentLoaded', function () {

    // Initialize Select2 for the doctorSelect dropdown
    $(document).ready(function () {
        $('#doctorSelect').select2();
    });

    fetchDoctors();

    // Set the min value of the date picker to today's date
    document.getElementById('datePicker').min = new Date().toLocaleDateString('en-CA');
    // Set the maximum date for the date picker to the end of next year
    setMaxDate();
    // Set the initial value of the date picker to today's date
    document.getElementById('datePicker').value = new Date().toLocaleDateString('en-CA');
});

function fetchDoctors(){
    // Fetch doctors from the API and populate the dropdown
    fetch('/api/doctor/getDoctors')
        .then(response => response.json())
        .then(doctors => {
            doctorsList = doctors;
            const doctorSelect = document.getElementById('doctorSelect');

            // Clear existing options
            doctorSelect.innerHTML = '';

            // Populate options based on the retrieved doctors
            doctors.forEach((doctor, index) => {
                const option = document.createElement('option');
                option.value = doctor.id;
                option.textContent = doctor.name; // Assuming the doctor object has a 'name' property
                doctorSelect.appendChild(option);

            });

            // Fetch timeslots for the initially selected doctor (on page load)
            fetchTimeslots();
        })
        .catch(error => console.error('Error fetching doctors:', error));

}
function fetchTimeslots() {
    const selectedDoctorId = document.getElementById('doctorSelect').value;
    const selectedDate = document.getElementById('datePicker').value;

    // Use the actual API endpoint to fetch timeslots for the selected doctor and all dates
    const apiUrl = `/api/doctor/getTimeSlotByDoctorId?doctorId=${selectedDoctorId}`;

    // Make the AJAX request
    fetch(apiUrl)
        .then(response => response.json())
        .then(allTimeslots => {
            // Filter timeslots based on the selected date
            const filteredTimeslots = allTimeslots.filter(timeslot => {
                const timeslotDate = new Date(timeslot.startTime).toISOString().split('T')[0];

                return timeslotDate === selectedDate;

            });

            // Convert the filtered timeslots to a format that can be used in the grid
            const timeslotStatuses = {};
            filteredTimeslots.forEach(timeslot => {
                // Format the start time to match the grid format
                const startTime = new Date(timeslot.startTime).toLocaleTimeString('en-US', {
                    hour: '2-digit',
                    minute: '2-digit',
                    hour12: true
                });

                // Map the status to the formatted start time
                timeslotStatuses[startTime] = timeslot.status;
            });

            // Populate the timeslot grid with the fetched timeslot statuses
            populateTimeslotGrid(timeslotStatuses);
        })
        .catch(error => console.error('Error fetching timeslots:', error));
}

function populateTimeslotGrid(timeslotStatus) {
    const gridContainer = document.getElementById('timeslotGridContainer');
    gridContainer.innerHTML = '';

    availableTimeslots.forEach(timeslot => {
        const timeslotBox = document.createElement('div');
        timeslotBox.classList.add('timeslot-box');

        if (timeslotStatus[timeslot] === 'Available') {
            timeslotBox.classList.add('available');

        } else if (timeslotStatus[timeslot] === 'Booked') {
            timeslotBox.classList.add('booked');
            timeslotBox.onclick = null; // Disable click on booked slots
        }
        else{
            timeslotBox.onclick = function () {
                toggleTimeslotStatus(timeslot, timeslotBox);
            };
        }

        timeslotBox.innerText = timeslot;
        gridContainer.appendChild(timeslotBox);
    });
}

function toggleTimeslotStatus(timeslot, timeslotBox) {
    // Disable changing status of timeslots received from the server
    // if (timeslotBox.classList.contains('booked')) {
    //     return;
    // }

    // Extract relevant data
    if (selectedTimeslot === timeslot) {
        // If the clicked timeslot is already selected, deselect it
        selectedTimeslot = null;
        timeslotBox.classList.remove('selected');
    } else {
        // If a different timeslot is clicked, update the selection
        if (selectedTimeslot) {
            const selectedBox = document.querySelector('.timeslot-box.selected');
            if (selectedBox && selectedBox.classList){
                selectedBox.classList.remove('selected');
            }

        }

        selectedTimeslot = timeslot;
        timeslotBox.classList.add('selected');
    }

}

function insertTimeslot() {
    if (!selectedTimeslot) {
        console.log('Please select a timeslot before submitting.');
        return;
    }

    // Extract relevant data
    const doctorId = document.getElementById('doctorSelect').value;
    const selectedDate = document.getElementById('datePicker').value;

    const selectedDoctor = doctorsList.find(doctor => doctor.id === parseInt(doctorId));
    // Combine the selected date and time
    const dateTimeString = `${selectedDate} ${selectedTimeslot}`;

    // Parse the combined date-time string
    const startTime = new Date(dateTimeString);
    const endTime = new Date(startTime);
    endTime.setMinutes(endTime.getMinutes() + 30);

    // Prepare the request body
    const requestBody = {
        doctor: selectedDoctor,
        startTime: startTime.toISOString(), // Convert to ISO string format
        endTime: endTime.toISOString(),
        booked: false
    };

    // Make the POST request to the server
    fetch('/api/doctor/createTimeSlot', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestBody),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to insert timeslot.');
            }
            console.log('Timeslot inserted successfully!');
            selectedTimeslot = null;
            fetchTimeslots();
            // You may want to refresh the timeslots grid or perform other actions here
        })
        .catch(error => {
            console.error('Error:', error.message);
            alert('Failed to insert timeslot.');
        });
}


function incrementDate() {
    const datePicker = document.getElementById('datePicker');
    const currentDate = new Date(datePicker.value);
    currentDate.setDate(currentDate.getDate() + 1);

    // Check if the incremented date exceeds the max date
    if (currentDate > new Date(document.getElementById('datePicker').max)) {
        return;
    }

    const formattedDate = currentDate.toISOString().split('T')[0];
    datePicker.value = formattedDate;
    fetchTimeslots();
}

function decrementDate() {
    const datePicker = document.getElementById('datePicker');
    const currentDate = new Date(datePicker.value).toLocaleDateString('en-CA');
    // currentDate.setDate(currentDate.getDate() - 1);

    // Ensure the date does not go below today's date
    const today = new Date().toLocaleDateString('en-CA');


    if (currentDate < today) {
        return;
    }

    datePicker.value = currentDate;
    fetchTimeslots();
}

function setMaxDate() {
    const datePicker = document.getElementById('datePicker');
    const endOfNextYear = new Date(new Date().getFullYear() + 1, 11, 31); // Set to the end of next year
    const formattedDate = endOfNextYear.toISOString().split('T')[0];
    datePicker.max = formattedDate;
}
