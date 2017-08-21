var root = "http://localhost:8080/";

function drawTable(data, drawRowFn) {
    $.each(data, function(key, val) {
        drawRowFn(val)
    });
}

function loadUnselected() {
    $.getJSON(root + "roster/notselected", function(data) {
        drawTable(data, drawUnSelectedRow);
    });
}

function loadSelected() {
    $.getJSON(root + "roster/selected", function(data) {
        drawTable(data, drawSelectedRow);

    });
}

function loadRecords() {
    $.getJSON(root + "/records", function(data) {})
        .done(function(data) {
            drawTable(data,drawRecordRow);
        });
}

function loadStatistics() {
    $.ajax({
            method: "GET",
            url: root + "statistics/current"
        })
        .done(function(data) {

            drawStatisticsRow(data);
        });
}

function loadHistory() {
    $.ajax({

            method: "GET",
            url: root + "statistics/history"       
        })
        .done(function(data) {

            drawStatisticsHistoryRow(data);
        });

}

function loadWaitlist() {
   $.ajax({

            method: "GET",
            url: root + "requests/wait"       
        })
        .done(function(data) {

            drawTable(data, drawWaitlistRow);
        });

}

function loadProcessedRequests() {
   $.ajax({

            method: "GET",
            url: root + "requests/processed"       
        })
        .done(function(data) {
            drawTable(data, drawProcessedRequestsRow);
        });

}

function loadState(input, nextLocation) {
    $.ajax({
            method: "POST",
            url: root + "state",
            // crossDomain: true,
            
            data: {
                "state": input
            }
        })
        .done(function(data) {
            window.location.href = nextLocation;
        });

}

function loadcurrentsemester(){
    var SemesterText = "Current Semester is:"
    
    $.ajax({
        url: root + 'semester/current',
        method: "GET",
        success: function(data) {          
            var label = $("<label>Current Semester is: " + data + "</label>");
            $("#topDiv").append(label);
        }
    });
}

function drawSelectedRow(rowData) {
    var row = $("<tr />")
    $("#selected").append(row);
    row.append($("<td>" + rowData.index + "</td>"))
    row.append($("<td>" + rowData.instructorId + "</td>"));
    row.append($("<td>" + rowData.courseId + "</td>"));
    row.append($("<td>" + rowData.capacity + "</td>"));
}

function drawUnSelectedRow(rowData) {
   var row = $("<tr />")
    row.append($("<td>" + rowData.index + "</td>"))
    row.append($("<td>" + rowData.instructorId + "</td>"));
    row.append($("<td>" + rowData.courseId + "</td>"));
    row.append($("<td>" + rowData.capacity + "</td>"));
    $("#unselected").append(row);
}

function drawRecordRow(rowData) {
    var row = $("<tr />")
    $("#recordstable").append(row);

    row.append($("<td>" + rowData.studentId.id + "</td>"));
    row.append($("<td>" + rowData.classId.id + "</td>"));
    row.append($("<td>" + rowData.instructorId.id + "</td>"));
    row.append($("<td>" + rowData.comments + "</td>"));
    row.append($("<td>" + rowData.grade + "</td>"));

}

function drawStatisticsRow(rowData) {
    var row = $("<tr />")

    $("#current").append(row);
    row.append($("<td>" + rowData.granted + "</td>"))
    row.append($("<td>" + rowData.failed + "</td>"));
    row.append($("<td>" + rowData.wait + "</td>"));
    row.append($("<td>" + rowData.total + "</td>"));
}

function drawStatisticsHistoryRow(rowData) {
    var row = $("<tr />")

    $("#history").append(row);
    row.append($("<td>" + rowData.granted + "</td>"))
    row.append($("<td>" + rowData.failed + "</td>"));
    row.append($("<td>" + rowData.wait + "</td>"));
    row.append($("<td>" + rowData.total + "</td>"));
}

function drawWaitlistRow(rowData) {
    var row = $("<tr />")
    $("#waitlisted").append(row);

    row.append($("<td>" + rowData.studentId.id + "</td>"));
    row.append($("<td>" + rowData.classId.id + "</td>"));
    row.append($("<td>" + rowData.status + "</td>"));
    row.append($("<td>" + rowData.statusMessage + "</td>"));


}

function drawProcessedRequestsRow(rowData) {
    var row = $("<tr />")
    $("#processedrecords").append(row);

    row.append($("<td>" + rowData.studentId.id + "</td>"));
    row.append($("<td>" + rowData.classId.id + "</td>"));
    row.append($("<td>" + rowData.status + "</td>"));
    row.append($("<td>" + rowData.statusMessage + "</td>"));


}

function assignInstructor() {

    var index = $("#indexId").val()
    $.ajax({
            method: "POST",
            url: root + "roster/select",
            crossDomain: true,
            data: {
                "index":index
            }
        })
        .done(function(msg) {
            alert("Data Saved: " + msg);
        });
    window.location.reload();
}

function removeInstructor() {
    var index = $("#indexIdremove").val()
    $.ajax({
            method: "POST",
            url: root + "roster/remove",
            crossDomain: true,
            data: {
                "index":index
            }
        })
        .done(function(msg) {
            alert("Data Saved: " + msg);
        });
    window.location.reload();
}

function gotoHomePage() {
}

function submit() {
    var submit = "submit"
    $.ajax({
            method: "POST",
            url: root + "roster/submit",
            crossDomain: true,
            data: {
                "action": 'submit'
            }
        })
        .done(function(data) {
            $('#submit').text(data);
            window.location.href='/statistics'
        });
}

function analyze() {
    $.ajax({
        url: root + '/analyze',
        method: "GET",
        success: function(data) {
            $('#show-data-apriori').text(data);
        }
    });
}