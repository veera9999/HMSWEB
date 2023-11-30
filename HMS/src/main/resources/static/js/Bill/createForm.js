let patientsList = null;
let appointmentsList = null;

document.addEventListener('DOMContentLoaded', function () {

    // Initialize Select2 for the patientSelect dropdown
    $(document).ready(function () {
        $('#patientSelect').select2();
    });

    // Initialize Select2 for the patientSelect dropdown
    $(document).ready(function () {
        $('#appointmentSelect').select2();
    });

    fetchPatients();
});

function fetchPatients() {
    fetch('/api/patient/getPatients')
        .then(response => response.json())
        .then(patientNames => {
            patientsList = patientNames;
            const patientSelect = document.getElementById('patientSelect');
            patientSelect.innerHTML = '';
            patientsList.forEach(patient => {
                const option = document.createElement('option');
                option.value = patient.id;
                option.textContent = patient.name;
                patientSelect.appendChild(option);
            });

            fetchAppointments()
        })
        .catch(error => console.error('Error fetching patients:', error));
}

function fetchAppointments() {
    const selectedPatientId = document.getElementById('patientSelect').value;
    const apiUrl = `/api/appointment/getPatientAppointments?patientId=${selectedPatientId}`;
    fetch(apiUrl)
        .then(response => response.json())
        .then(appointments => {
            appointmentsList = appointments;
            const appointmentSelect = document.getElementById('appointmentSelect');
            appointmentSelect.innerHTML = '';
            appointments.forEach(appointment => {
                const startTime = new Date(appointment.timeSlot.startTime).toLocaleTimeString('en-US', {
                    hour: '2-digit',
                    minute: '2-digit',
                    hour12: true
                });

                const option = document.createElement('option');
                option.value = appointment.id;
                option.textContent = `${appointment.doctor.name} - ${startTime}`;
                appointmentSelect.appendChild(option);
            });


        })
        .catch(error => console.error('Error fetching timeslots:', error));
}

function generateBill() {

    const appointmentSelectId = document.getElementById('appointmentSelect').value;

    const selectedAppointment = appointmentsList.find(appointment => appointment.id === parseInt(appointmentSelectId));

    const description = document.getElementById('description').value;
    const dueAmount = document.getElementById('dueAmount').value;

    const startTime = new Date(selectedAppointment.timeSlot.startTime).toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
    });

    const requestBody = {
        appointment: selectedAppointment,
        description: description,
        due_amount: parseFloat(dueAmount)
    };

    fetch('/api/bill/createBill', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
    })
    .then(data => {
        alert('Bill generated successfully!');
        document.getElementById('description').value = '';
        document.getElementById('dueAmount').value = '';
        fetchPatients();
    })
    .catch(error => console.error('Error generating bill:', error));
}
