const birthdateId = 'birthdate';
const dateSeparator = 'T';

function setMaxDate() {
    document.getElementById(birthdateId).max = new Date().toISOString().split(dateSeparator)[0];
}
