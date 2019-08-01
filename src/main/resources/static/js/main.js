function appendHeader(table, key, desc) {
  addRowToTable(table, key, desc, ' head');
}

function addRow(table, key, desc) {
  addRowToTable(table, key, desc, '');
}

function addRowToTable(table, key, desc, classes) {
  var row = $('<tr>').addClass('row' + classes);
  var key = $('<td>').addClass('col').text(key);
  var desc = $('<td>').addClass('col').text(desc);

  row.append(key);
  row.append(desc);

  table.append(row);
}

function setResult(content) {
  $('#validation-result-container').html(content);
}

$('#validate-form').submit(function (event) {
  var formData = new FormData(this);

  $.ajax({
    type: "POST",
    enctype: 'multipart/form-data',
    url: $(location).attr("href") + "processFile",
    data: formData,
    processData: false,
    contentType: false,
    success: function (response) {
      console.log(response);

      var content;

      if (response.validated) {
        content = $('<text>').addClass('validated').text('All  records are  valid!!');
      } else {
        content = $('<table>').addClass('errors');
        appendHeader(content, 'Invalid Reference Number ', 'Description');
        for (i = 0; i < response.validationErrors.length; i++) {
          addRow(content, response.validationErrors[i].errorKey, response.validationErrors[i].errorDescription);
        }
      }

      setResult(content);
    },
    error: function (error) {
      console.log(error);

      setResult($('<text>').addClass('error').text('Error Response Status Code: ' + error.status));
    }
  });

  event.preventDefault();
});