
function searchDoctors() {
    var doctorName = document.getElementById("doctorName").value;

    // Make sure doctorName is not empty
    if (doctorName.trim() === "") {
        alert("Please enter a doctor name.");
        return;
    }

    // Make the AJAX request to your API endpoint
    fetch(`/api/doctor/searchdoctors?name=${encodeURIComponent(doctorName)}`)
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                // Doctors found, update the table
                updateDoctorTable(data);
            } else {
                // No doctors found, display a message
                updateDoctorTable([]);
                // alert("No doctors found with the given name.");
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function updateDoctorTable(data) {
    var doctorTableBody = document.getElementById('doctorTableBody');
    doctorTableBody.innerHTML = '';

    for (var i = 0; i < data.length; i++) {
        var row = '<tr>' +
            '<td>' + data[i].id + '</td>' +
            '<td>' + data[i].name + '</td>' +
            '<td>' + data[i].age + '</td>' +
            '<td>' + data[i].specialization + '</td>' +
            '<td>' + data[i].gender + '</td>' +
            '<td>' + data[i].phoneNumber + '</td>' +
            '<td>' + data[i].email + '</td>' +
            '<td><button type="button" onclick="editDoctor(' + data[i].id + ')">Edit</button></td>' +
            '<td><button type="button" onclick="deleteDoctor(' + data[i].id + ')">Delete</button></td>' +
            '</tr>';
        doctorTableBody.innerHTML += row;
    }
}


function editDoctor(doctorId) {
    // Get the table body element
    var doctorTableBody = document.getElementById('doctorTableBody');

    // Find the selected row by iterating through the rows
    var selectedRow;
    for (var i = 0; i < doctorTableBody.rows.length; i++) {
        var row = doctorTableBody.rows[i];
        var idCell = row.cells[0]; // Assuming the first cell contains the ID

        // Check if the ID in the row matches the selected doctorId
        if (idCell.innerText == doctorId) {
            selectedRow = row;
            break;
        }
    }

    // Check if a row is found
    if (selectedRow) {
        // Extract data from the selected row and populate the edit form
        var selectedRowData = {
            id: selectedRow.cells[0].innerText,
            name: selectedRow.cells[1].innerText,
            age: selectedRow.cells[2].innerText,
            specialization: selectedRow.cells[3].innerText,
            gender: selectedRow.cells[4].innerText,
            phoneNumber: selectedRow.cells[5].innerText,
            email: selectedRow.cells[6].innerText
        };

        populateEditForm(selectedRowData);

    } else {
        console.error('Selected row not found');
    }
}

function populateEditForm(data) {
    // Populate the editable form fields with doctor details
    document.getElementById('editName').value = data.name;
    document.getElementById('editAge').value = data.age;
    document.getElementById('editSpecialization').value = data.specialization;
    document.getElementById('editGender').value = data.gender;
    document.getElementById('editPhoneNumber').value = data.phoneNumber;
    document.getElementById('editEmail').value = data.email;

    // Add doctorId to the onclick attribute of the Update button
    var updateButton = document.getElementById('updateButton');
    updateButton.setAttribute('onclick', 'updateDoctor(' + data.id + ')');

    // Show the edit form
    document.getElementById('doctorEditForm').style.display = 'block';
}

function updateDoctor(doctorId) {
    var doctorId = doctorId
    var doctorName = document.getElementById('editName').value;
    var doctorAge = document.getElementById('editAge').value;
    var doctorSpecialization = document.getElementById('editSpecialization').value;
    var doctorGender = document.getElementById('editGender').value;
    var doctorPhoneNumber = document.getElementById('editPhoneNumber').value;
    var doctorEmail = document.getElementById('editEmail').value;

    // Your AJAX logic to update the doctor details
    fetch('/api/doctor/updatedoctor', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: parseInt(doctorId),
            name: doctorName,
            age: parseInt(doctorAge),
            specialization: doctorSpecialization,
            gender: doctorGender,
            phoneNumber: doctorPhoneNumber,
            email: doctorEmail,
        }),
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to update doctor.');
        }
        // Show a confirmation message
        alert('Doctor updated successfully!');
        searchDoctors();
        // Hide the edit form after updating
        document.getElementById('doctorEditForm').style.display = 'none';
    })
    .catch(error => {
        console.error('Error:', error.message);
        alert('Failed to update doctor.');
    });
}

function deleteDoctor(doctorId) {
    // Ask for confirmation before deleting
    var confirmDelete = window.confirm("Are you sure you want to delete this doctor?");

    if (!confirmDelete) {
        return; // User cancelled the deletion
    }
    // Make the AJAX request to delete the doctor
    fetch(`/api/doctor/deletedoctor?id=${doctorId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to delete doctor with ID ${doctorId}`);
            }
            // Check if the response body is empty
            const contentType = response.headers.get('content-type');
            if (contentType && contentType.includes('application/json')) {
                return response.json(); // Parse JSON if present
            } else {
                return null; // No JSON data, return null
            }
        })
        .then(data => {
            // Show a confirmation message
            alert('Doctor deleted successfully!');
            searchDoctors();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to delete doctor.');
        });
}


function restoreDoctor() {
    // Your AJAX logic to restore the doctor
    fetch(`/api/doctor/restoredoctor`, {
        method: 'POST', // Assuming you use POST for restore
    })
        .then(response => {
            if (response.ok) {
                // Success status (e.g., HTTP 200)
                alert('Doctor restored successfully!');
                searchDoctors(); // Optionally refresh the doctor list
            } else if (response.status === 404) {
                // No doctors to restore (HTTP 404 Not Found)
                alert('No doctors to restore.');
            } else {
                // Error status (e.g., HTTP 4xx or 5xx)
                alert('Failed to restore doctor.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to restore doctor.'); // Handle network errors
        });
}