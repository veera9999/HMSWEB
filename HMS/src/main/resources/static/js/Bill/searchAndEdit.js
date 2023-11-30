function formatDate(dateString) {
    var date = new Date(dateString);
    return date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
}

function searchBillsByPatientName() {
    var patientName = document.getElementById("patientName").value;

    if (patientName.trim() === "") {
        alert("Please enter a Patient Name.");
        return;
    }

    fetch(`/api/bill/searchBills?patientName=${encodeURIComponent(patientName)}`)
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                updateBillTable(data);
            } else {
                updateBillTable([]);
                alert("No Bills found with the given name.");
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function updateBillTable(data) {
    var billTableBody = document.getElementById('billTableBody');
    billTableBody.innerHTML = '';

    for (var i = 0; i < data.length; i++) {
        var row = '<tr>' +
            '<td>' + data[i].id + '</td>' +
            '<td>' + data[i].appointment.patient.name + '</td>' +
            '<td>' + data[i].appointment.doctor.name + '</td>' +
            '<td>' + formatDate(data[i].appointment.timeSlot.startTime) + '</td>' +
            '<td>' + data[i].description + '</td>' +
            '<td>' + data[i].due_amount + '</td>' +
            '<td><button type="button" onclick="editBill(' + data[i].id + ', ' + data[i].appointment.id + ')">Edit</button></td>' +
            '<td><button type="button" onclick="deleteBill(' + data[i].id + ')">Delete</button></td>' +
            '</tr>';
        billTableBody.innerHTML += row;
    }
}

function editBill(billId, appointmentId) {
    var billTableBody = document.getElementById('billTableBody');
    var selectedRow;

    for (var i = 0; i < billTableBody.rows.length; i++) {
        var row = billTableBody.rows[i];
        var idCell = row.cells[0];

        if (idCell.innerText == billId) {
            selectedRow = row;
            break;
        }
    }

    if (selectedRow) {
        var selectedRowData = {
            id: selectedRow.cells[0].innerText,
            patientName: selectedRow.cells[1].innerText,
            doctorName: selectedRow.cells[2].innerText,
            timeSlot: selectedRow.cells[3].innerText,
            description: selectedRow.cells[4].innerText,
            due_amount: selectedRow.cells[5].innerText,
        };

        populateEditForm(selectedRowData, billId, appointmentId);

    } else {
        console.error('Selected row not found');
    }
}

function populateEditForm(data, billId, appointmentId) {
    document.getElementById('editPatientName').value = data.patientName;
    document.getElementById('editDoctorName').value = data.doctorName;
    document.getElementById('editTimeSlot').value = data.timeSlot;
    document.getElementById('editDescription').value = data.description;
    document.getElementById('editDueAmount').value = data.due_amount;

    var updateButton = document.getElementById('updateButton');
    updateButton.setAttribute('onclick', 'updateBill(' + billId + ', ' + appointmentId + ')');
    var editForm = document.getElementById("billEditForm");
    if (editForm) {
        editForm.style.display = 'block';
    } else {
        console.error('Edit form not found');
    }
}

function updateBill(billId, appointmentId) {
    var billDescription = document.getElementById('editDescription').value;
    var billDueAmount = document.getElementById('editDueAmount').value;

    // Constructing the JSON body with the required structure
    var billData = {
        id: billId,
        appointment: { id: appointmentId }, // Nested object for appointment with just the id
        description: billDescription,
        due_amount: parseFloat(billDueAmount)
    };

    console.log("Updating Bill with data:", JSON.stringify(billData));

    fetch('/api/bill/updateBill', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(billData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to update Bill.');
        }
        alert('Bill updated successfully!');
        searchBillsByPatientName();
        document.getElementById('billEditForm').style.display = 'none';
    })
    .catch(error => {
        console.error('Error:', error.message);
        alert('Failed to update bill.');
    });
}

function deleteBill(billId) {
    var confirmDelete = window.confirm("Are you sure you want to delete this bill?");

    if (!confirmDelete) {
        return;
    }

    fetch(`/api/bill/deleteBill?id=${billId}`, {
        method: 'DELETE',
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Failed to delete bill with ID ${billId}`);
        }
        if (response.status === 204 || response.status === 200) {
            alert('Bill deleted successfully!');
            searchBillsByPatientName();
        } else {
            return response.json();
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Failed to delete bill.');
    });
}

function restoreBill() {
    fetch(`/api/bill/restoreBill`, {
        method: 'POST',
    })
        .then(response => {
            if (response.ok) {
                alert('Bill restored successfully!');
                searchBillsByPatientName();
            } else if (response.status === 404) {
                alert('No bills to restore.');
            } else {
                alert('Failed to restore bill.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to restore bill.');
        });
}
