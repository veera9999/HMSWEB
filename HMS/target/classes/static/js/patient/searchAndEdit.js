

function searchPatients() {
    var patientName = document.getElementById("patientName").value;

    // Make sure patientName is not empty
    if (patientName.trim() === "") {
        alert("Please enter a patient name.");
        return;
    }

    // Make the AJAX request to your API endpoint
    fetch(`/api/patient/searchpatients?name=${encodeURIComponent(patientName)}`)
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                // Patients found, update the table
                updatePatientTable(data);
            } else {
                // No patients found, display a message
                updatePatientTable([]);
                // alert("No patients found with the given name.");
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function updatePatientTable(data) {
    var patientTableBody = document.getElementById('patientTableBody');
    patientTableBody.innerHTML = '';

    for (var i = 0; i < data.length; i++) {
        var row = '<tr>' +
            '<td>' + data[i].id + '</td>' +
            '<td>' + data[i].name + '</td>' +
            '<td>' + data[i].age + '</td>' +
            '<td>' + data[i].gender + '</td>' +
            '<td>' + data[i].phoneNumber + '</td>' +
            '<td>' + data[i].email + '</td>' +
            '<td><button type="button" onclick="editPatient(' + data[i].id + ')">Edit</button></td>' +
            '<td><button type="button" onclick="deletePatient(' + data[i].id + ')">Delete</button></td>' +
            '</tr>';
        patientTableBody.innerHTML += row;
    }
}


function editPatient(patientId) {
    // Get the table body element
    var patientTableBody = document.getElementById('patientTableBody');

    // Find the selected row by iterating through the rows
    var selectedRow;
    for (var i = 0; i < patientTableBody.rows.length; i++) {
        var row = patientTableBody.rows[i];
        var idCell = row.cells[0]; // Assuming the first cell contains the ID

        // Check if the ID in the row matches the selected patientId
        if (idCell.innerText == patientId) {
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
            gender: selectedRow.cells[3].innerText,
            phoneNumber: selectedRow.cells[4].innerText,
            email: selectedRow.cells[5].innerText
        };

        populateEditForm(selectedRowData);

    } else {
        console.error('Selected row not found');
    }
}

function populateEditForm(data) {
    // Populate the editable form fields with patient details
    document.getElementById('editName').value = data.name;
    document.getElementById('editAge').value = data.age;
    document.getElementById('editGender').value = data.gender;
    document.getElementById('editPhoneNumber').value = data.phoneNumber;
    document.getElementById('editEmail').value = data.email;

    // Add patientId to the onclick attribute of the Update button
    var updateButton = document.getElementById('updateButton');
    updateButton.setAttribute('onclick', 'updatePatient(' + data.id + ')');

    // Show the edit form
    document.getElementById('patientEditForm').style.display = 'block';
}

function updatePatient(patientId) {
    var patientId = patientId
    var patientName = document.getElementById('editName').value;
    var patientAge = document.getElementById('editAge').value;
    var patientGender = document.getElementById('editGender').value;
    var patientPhoneNumber = document.getElementById('editPhoneNumber').value;
    var patientEmail = document.getElementById('editEmail').value;

    // Your AJAX logic to update the patient details
    fetch('/api/patient/updatepatient', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            id: parseInt(patientId),
            name: patientName,
            age: parseInt(patientAge),
            gender: patientGender,
            phoneNumber: patientPhoneNumber,
            email: patientEmail,
        }),
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to update patient.');
            }
            // Show a confirmation message
            alert('Patient updated successfully!');
            searchPatients();
            // Hide the edit form after updating
            document.getElementById('patientEditForm').style.display = 'none';
        })
        .catch(error => {
            console.error('Error:', error.message);
            alert('Failed to update patient.');
        });
}

function deletePatient(patientId) {
    // Ask for confirmation before deleting
    var confirmDelete = window.confirm("Are you sure you want to delete this patient?");

    if (!confirmDelete) {
        return; // User cancelled the deletion
    }
    // Make the AJAX request to delete the patient
    fetch(`/api/patient/deletepatient?id=${patientId}`, {
        method: 'DELETE',
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Failed to delete patient with ID ${patientId}`);
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
            alert('Patient deleted successfully!');
            searchPatients();
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to delete patient.');
        });
}


function restorePatient() {
    // Your AJAX logic to restore the patient
    fetch(`/api/patient/restorepatient`, {
        method: 'POST', // Assuming you use POST for restore
    })
        .then(response => {
            if (response.ok) {
                // Success status (e.g., HTTP 200)
                alert('Patient restored successfully!');
                searchPatients(); // Optionally refresh the patient list
            } else if (response.status === 404) {
                // No patients to restore (HTTP 404 Not Found)
                alert('No patients to restore.');
            } else {
                // Error status (e.g., HTTP 4xx or 5xx)
                alert('Failed to restore patient.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to restore patient.'); // Handle network errors
        });
}
