$(document).ready(async function () {
    await getPrincipal()
    await getData();
});

var allRoles
var allUsers
var principal


//получение залогиненного пользователя
async function getPrincipal() {
    let jsonPrincipal = await fetch('/api/user');

    principal = await jsonPrincipal.json();
    fillNavbar()
}

//оформление навбара
function fillNavbar() {
    $('#principalView').empty()
    $('#principalView').append(principal.stringView)
}

//получение всех пользователей и всех ролей
async function getData(){
    let roles = await fetch('/api/roles');
    allRoles = await roles.json();


    let users = await fetch('/api/admin');
    allUsers = await users.json();

    fillTable()
}

//заполнение таблицы админа

function fillTable() {
    $('#data').empty();

    $.each(allUsers, function(i, user) {
        $('<tr>').append(
            $('<td>').text(user.id),
            $('<td>').text(user.firstName),
            $('<td>').text(user.lastName),
            $('<td>').text(user.age),
            $('<td>').text(user.username),
            $('<td>').text((user.roles).map(role => role.shortRole).join(', ')),
            $('<td>').append($('<button>').text("Edit").attr({
                "type": "button",
                "id": "buttonEdit",
                "class": "btn btn-info",
                "data-bs-toggle": "modal",
                "data-bs-target": "#editModal",
            }).data("user", user)),
            $('<td>').append($('<button>').text("Delete").attr({
                "type": "button",
                "id": "buttonDelete",
                "class": "btn btn-danger",
                "data-bs-toggle": "modal",
                "data-bs-target": "#deleteModal",
            }).data("user", user))
        ).appendTo('.users-table-body')
    })
}


//конструирование модалки изменения пользователя

$(document).on('click', '#buttonEdit', function () {
    $(`#rolesToEdit`).empty()

    let user = $(this).data("user");
    $('#idToEdit').val(user.id)
    $('#firstNameToEdit').val(user.firstName)
    $('#lastNameToEdit').val(user.lastName)
    $('#ageToEdit').val(user.age)
    $('#usernameToEdit').val(user.username)
    $('#passwordToEdit').val(user.password)

    let selected = false

    $.each(allRoles, function(i, role) {

        $.each(user.roles, function (i, userRole) {
            if (userRole.name === role.name)
                selected = true
        })

        $(`#rolesToEdit`).append(
            $('<option>').text(role.shortRole).attr({
                "id": role.id,
                "value": role.name,
                "selected": selected
            })
        )
        selected = false
    })
})

//запрос на изменение пользователя
$(document).on('click', '#submit-edit', async function () {

    let roles = [];
    $('#rolesToEdit option:selected').each(function(index, value) {
        roles[index] = {
            id: value.id,
            role: value.name
        }
    });

    const user = {
        id: $('#idToEdit').val(),
        firstName: $('#firstNameToEdit').val(),
        lastName: $('#lastNameToEdit').val(),
        age: $('#ageToEdit').val(),
        username: $('#usernameToEdit').val(),
        password: $('#passwordToEdit').val(),
        roles: roles
    };

    try {
        const response = await fetch('/api/admin/edit', {
            method: 'PATCH',
            body: JSON.stringify(user),
            headers: {
                'Content-Type': 'application/json'
            }
        });

        $('#edit-close-button').click();
        await getData();
        await getPrincipal();

        const json = await response.json();
        console.log('Успех:', JSON.stringify(json));
    } catch (error) {
        console.error('Ошибка:', error);
    }
})



// конструирование модалки удаления пользователя

$(document).on('click', '#buttonDelete', function () {
    $(`#rolesToDelete`).empty()

    let user = $(this).data("user");
    $('#idToDelete').val(user.id)
    $('#firstNameToDelete').val(user.firstName)
    $('#lastNameToDelete').val(user.lastName)
    $('#ageToDelete').val(user.age)
    $('#usernameToDelete').val(user.username)


    $.each(user.roles, function (i, userRole) {

        $(`#rolesToDelete`).append(
            $('<option>').text(userRole.shortRole).attr({
                "id": userRole.id,
                "value": userRole.name,
                "selected": "true",
            })
        )
    })
})

//запрос на удаление пользователя
$(document).on('click', '#submit-delete', async function () {

    try {
        const response = await fetch('/api/admin/user/' + $('#idToDelete').val(), {
            method: 'DELETE',
        });

        $('#delete-button-modal').click();
        await getData();

        const json = await response.json();
        console.log('Успех:', JSON.stringify(json));
    } catch (error) {
        console.error('Ошибка:', error);
    }
})



//конструирование формы создания НОВОГО ЮЗЕРА

$(document).on('click', '#nav-profile-tab', function () {
    $(`#newUserRoles`).empty()
    let newSelected = false
    $.each(allRoles, function(i, role) {
        if (role.name === "ROLE_USER") {
            newSelected = true
        }
        $(`#newUserRoles`).append(
            $('<option>').text(role.shortRole).attr({
                "id": role.id,
                "value": role.name,
                "selected": newSelected
            })
        )
    })
})

//запрос на создание нового юзера
$(document).on('click', '#createUser', async function () {
    let roles = [];
    $('#newUserRoles option:selected').each(function(index, value) {
        roles[index] = {
            id: value.id,
            role: value.name
        }
    });

    const user = {
        firstName: $('#firstName').val(),
        lastName: $('#lastName').val(),
        age: $('#age').val(),
        username: $('#username').val(),
        password: $('#password').val(),
        roles: roles
    };

    const response = await fetch('/api/admin/newUser', {
        method: 'POST',
        body: JSON.stringify(user),
        headers: {
            'Content-Type': 'application/json'
        }
    });

    const json = await response.json();
    $('.nav-tabs a[href="#nav-home"]').tab('show');

    await getData();
})


//таб панель с информацией о залогиненом пользователе
$(document).on('click', '#list-profile-list', function () {
    $(`#principal`).empty()
    $('<tr>').append(
        $('<td>').text(principal.id),
        $('<td>').text(principal.firstName),
        $('<td>').text(principal.lastName),
        $('<td>').text(principal.age),
        $('<td>').text(principal.username),
        $('<td>').text((principal.roles).map(role => role.shortRole).join(', ')),
    ).appendTo('.principal-table')
})