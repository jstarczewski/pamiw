const confirmPasswordLabelId = 'confirm_password_label';
const spanElement = 'span';
const differentPasswordErrorTextSpanId = 'error_span';
const errorSpanClassName = 'error_span';
const differentPasswordErrorText = 'Passwords do not match constraints';
const passwordId = 'password';
const passwordConfirmationId = 'confirm_password';
const submitId = 'submit';
const nameId = 'firstname';
const nameLabelId = 'firstname_label';
const charactersErrorText = 'Invalid characters in input field';
const notValidNameSpanId = 'name_error_span';
const pesel = 'pesel';
const loginId = 'login';
const birthdateId = 'birthdate';
const dateSeparator = 'T';
const emptyErrorText = 'Field cannot be empty';
const formId = 'form';

function init() {
    const submitButton = document.getElementById(submitId);
    setMaxDate();
    if (document.getElementById(loginId).value !== '')
        checkLogin()
}

function checkErrorSpans() {
    const submitButton = document.getElementById(submitId);
    if (document.getElementsByClassName(errorSpanClassName).length === 0) {
        submitButton.disabled = false;
    } else {
        submitButton.disabled = true;
    }
}

function checkPasswordsEquality() {
    const password = document.getElementById(passwordId).value;
    const confirmedPassword = document.getElementById(passwordConfirmationId).value;
    if (password === confirmedPassword && password.length >= 8 && /^[a-z]+$/.test(password)) {
        deleteErrorSpan(differentPasswordErrorTextSpanId);
    } else {
        addErrorSpan(differentPasswordErrorTextSpanId, confirmPasswordLabelId, differentPasswordErrorText);
    }
}

function checkNameCorrectness() {
    if (isTextFieldContainsValidCharacters(nameId)) {
        deleteErrorSpan(notValidNameSpanId);
    } else {
        addErrorSpan(notValidNameSpanId, nameLabelId, charactersErrorText)
    }
}

const lastNameId = 'lastname';
const notValidLastNameSpanId = 'lastname_error_span';
const lastNameLabelId = 'lastname_label';

function checkLastNameCorrectness() {
    if (isTextFieldContainsValidCharacters(lastNameId)) {
        deleteErrorSpan(notValidLastNameSpanId);
    } else {
        addErrorSpan(notValidLastNameSpanId, lastNameLabelId, charactersErrorText);
    }
}

const peselId = 'pesel';
const peselLabelId = 'pesel_label';
const notValidPeselSpanId = 'not_valid_pesel';
const notValidPeselText = 'PESEL is not valid';

function checkPesel() {
    const pesel = document.getElementById(peselId);
    const pesel_value = pesel.value;
    if (isTextFieldContainsOnlyNumbers(peselId) && pesel_value.length === 11 && checkPeselMath(pesel_value)) {
        deleteErrorSpan(notValidPeselSpanId);
    } else {
        addErrorSpan(notValidPeselSpanId, peselLabelId, notValidPeselText);
    }
}

function checkPeselMath(peselNumber) {
    const weights = [9, 7, 3, 1];
    let sum = 0;
    for (let i = 0; i < 10; i++) {
        sum += weights[i % 4] * parseInt(peselNumber[i])
    }
    const date = document.getElementById(birthdateId).valueAsDate;
    var monthCondition = false;
    if (date.getFullYear() < 2000)
        monthCondition = date.getMonth() + 1 === (parseInt(peselNumber[2]) * 10 + parseInt(peselNumber[3]))
    else
        monthCondition = date.getMonth() + 1 === parseInt(peselNumber[2] * 10 + parseInt(peselNumber[3]) + 20)
    return sum % 10 === parseInt(peselNumber[10]) &&
        date.getDate() === parseInt(peselNumber[4]) * 10 + parseInt(peselNumber[5]) &&
        date.getFullYear() % 100 === parseInt(peselNumber[0]) * 10 + parseInt(peselNumber[1]) && monthCondition
}


function isTextFieldContainsOnlyNumbers(elementId) {
    const element = document.getElementById(elementId);
    return (/\d+$/.test(element.value))
}

const photoId = 'photo';
const photoLabelId = 'photo_label';
const notValidPhotoSpanId = 'not_valud_photo';
const notValidPhotoText = 'Attached file is not valid';

function checkPhoto() {
    const photo = document.getElementById(photoId);
    if (photo.value === '') {
        addErrorSpan(notValidPhotoSpanId, photoLabelId, notValidPhotoText)
    } else {
        deleteErrorSpan(notValidPhotoSpanId)
    }
}

function isTextFieldContainsValidCharacters(fieldNameId) {
    const name = document.getElementById(fieldNameId).value;
    if (!/^[AaĄąBbCcĆćDdEeĘęFfGgHhIiJjKkLlŁłMmNnŃńOoÓóPpRrSsŚśTtUuWwYyZzŹźŻżXxQq]+$/.test(name) ||
        name[0] !== name[0].toUpperCase()) {
        return false;
    } else {
        return true;
    }
}

const notValidDateErrorText = "Date is not valid";
const dateLabelId = "date_label";
const notValidDateSpanId = 'date_error_span';

function checkDate() {
    let date = document.getElementById(birthdateId);
    if (!Date.parse(date.value) ||
        date.value <= date.min || date.value >= date.max) {
        addErrorSpan(notValidDateSpanId, dateLabelId, notValidDateErrorText);
    } else {
        deleteErrorSpan(notValidDateSpanId);
    }
}

function addErrorSpan(errorSpanId, parentId, text) {
    if (document.getElementById(errorSpanId) == null) {
        document.getElementById(parentId).appendChild(
            createErrorSpan(text, errorSpanId)
        )
    }
    checkErrorSpans()
}

function deleteErrorSpan(errorSpanId) {
    const elementToRemove = document.getElementById(errorSpanId);
    if (elementToRemove != null) {
        elementToRemove.remove()
    }
    checkErrorSpans()
}

function createErrorSpan(innerText, spanId) {
    let errorSpan = document.createElement(spanElement);
    errorSpan.id = spanId;
    errorSpan.className = errorSpanClassName;
    errorSpan.innerText = innerText;
    return errorSpan;
}

function setMaxDate() {
    document.getElementById(birthdateId).max = new Date().toISOString().split(dateSeparator)[0];
}

const submitLabelId = 'submit_label';
const submitRequestErrorSpanId = 'request_error';
const userExists = 'User already exists';
const serverError = 'There was an error on server side';
const transactionError = 'There was an error during transaction';
const loginLabelId = 'login_label';
const loginLabelErrorSpanId = 'login_error_span';
const emptyFields = 'Login does not match constraints';

function checkLogin() {
    const loginElement = document.getElementById(loginId);
    const loginText = loginElement.value;
    if (loginText.length >= 3 && loginText.length <= 12 && /^[a-z]+$/.test(loginText)) {
        deleteErrorSpan(loginLabelErrorSpanId);
        checkUser(loginText)
    } else {
        addErrorSpan(loginLabelErrorSpanId, loginLabelId, emptyFields);
    }
}

function checkUser(userName) {
    let url = 'http://localhost:5000/user/' + userName;
    let request = new XMLHttpRequest();
    const submitButton = document.getElementById(submitId);
    request.open('GET', url);
    request.onload = function () {
        if (request.status === 404) {
            deleteErrorSpan(loginLabelErrorSpanId);
            submitButton.disabled = false;
        } else if (request.status === 500) {
            addErrorSpan(loginLabelErrorSpanId, loginLabelId, serverError);
            submitButton.disabled = true;
        } else if (request.status === 200) {
            addErrorSpan(loginLabelErrorSpanId, loginLabelId, userExists);
            submitButton.disabled = true;
        } else {

        }
    };
    request.onerror = function () {
        addErrorSpan(submitRequestErrorSpanId, submitLabelId, transactionError);
    };
    request.send();
}
