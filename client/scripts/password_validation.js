const confirmPasswordLabelId = 'confirm_password_label';
const spanElement = 'span';
const differentPasswordErrorTextSpanId = 'error_span';
const errorSpanClassName = 'error_span';
const differentPasswordErrorText = 'Passwords are not equal or are too short';
const passwordId = 'password';
const passwordConfirmationId = 'confirm_password';
const submitId = 'submit';
const nameId = 'firstname';
const nameLabelId = 'firstname_label';
const charactersErrorText = 'Invalid characters';
const notValidNameSpanId = 'name_error_span';
const pesel = 'pesel';
const login = 'login';

function checkPasswordsEquality() {
    const password = document.getElementById(passwordId).value;
    const confirmedPassword = document.getElementById(passwordConfirmationId).value;
    const submitButton = document.getElementById(submitId);
    if (password === confirmedPassword && password.length >= 8) {
        deleteErrorSpan(differentPasswordErrorTextSpanId);
        submitButton.disabled = false;
    } else {
        submitButton.disabled = true;
        addErrorSpan(differentPasswordErrorTextSpanId, confirmPasswordLabelId, differentPasswordErrorText);
    }
}

function validateNameCorrectness() {
    if (isTextFieldContainsValidCharacters(nameId)) {
        deleteErrorSpan(notValidNameSpanId)
    } else {
        addErrorSpan(notValidNameSpanId, nameLabelId, charactersErrorText)
    }
}

const lastNameId = 'lastname';
const notValidLastNameSpanId = 'lastname_error_span';
const lastNameLabelId = 'lastname_label';

function validateLastNameCorrectness() {
    if (isTextFieldContainsValidCharacters(lastNameId)) {
        deleteErrorSpan(notValidLastNameSpanId);
    } else {
        addErrorSpan(notValidLastNameSpanId, lastNameLabelId, charactersErrorText);
    }
}

function isTextFieldContainsValidCharacters(fieldNameId) {
    const name = document.getElementById(fieldNameId).value;
    const submitButton = document.getElementById(submitId);
    if (!/^[AaĄąBbCcĆćDdEeĘęFfGgHhIiJjKkLlŁłMmNnŃńOoÓóPpRrSsŚśTtUuWwYyZzŹźŻżXxQq]+$/.test(name)) {
        submitButton.disabled = true;
        return false;
    } else {
        submitButton.disabled = false;
        return true;
    }
}

function isEmpty() {
    if (document.getElementById(nameId).value === "" ||
        document.getElementById(lastNameId).value === "" ||
        document.getElementById(passwordId).value === "" ||
        document.getElementById(passwordConfirmationId).value === "" ||
        document.getElementById(pesel).value === "" ||
        document.getElementById(login).value === "" ||
        !Date.parse(document.getElementById('birthdate').value) ||
        document.getElementById('photo').files.length === 0
    ) {
        return true;
    } else {
        return false;
    }
}

const birthdateId = 'birthdate';
const dateSeparator = 'T';

function setMaxDate() {
    document.getElementById(birthdateId).max = new Date().toISOString().split(dateSeparator)[0];
}

function validateDate() {
    let date = document.getElementById('birthdate');
    if (!Date.parse(date.value) ||
        date.value <= date.min || date.value >= date.max) {
        addErrorSpan(notValidLastNameSpanId, lastNameLabelId, charactersErrorText);
    } else {
        deleteErrorSpan(notValidLastNameSpanId);
    }
}

function addErrorSpan(errorSpanId, parentId, text) {
    if (document.getElementById(errorSpanId) == null) {
        document.getElementById(parentId).appendChild(
            createErrorSpan(text, errorSpanId)
        )
    }
}

function deleteErrorSpan(errorSpanId) {
    document.getElementById(errorSpanId).remove();
}

function createErrorSpan(innerText, spanId) {
    let errorSpan = document.createElement(spanElement);
    errorSpan.id = spanId;
    errorSpan.className = errorSpanClassName;
    errorSpan.innerText = innerText;
    return errorSpan;
}

function checkSubmitButtonAvailability() {
    const submitButton = document.getElementById(submitId);
    submitButton.disabled = !!(isEmpty() || document.getElementById(notValidLastNameSpanId) != null ||
        document.getElementById(notValidNameSpanId) != null ||
        document.getElementById(differentPasswordErrorTextSpanId) != null)
}

function setup() {

}
