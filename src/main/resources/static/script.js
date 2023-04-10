// фильтрация строк таблицы
$(document).ready(function () {
    $("#my-search-input").on('keyup', function () {
        let value = $(this).val().toLowerCase();
        $("#table-body tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});

// апдейт
$(document).ready(function () {
    $('form').on('submit', async function (event) {
        event.preventDefault();

        let user = {
            id: $('input[name=id]').val(),
            username: $('input[name=username]').val(),
            password: $('input[name=password]').val(),
            name: $('input[name=name]').val(),
            lastName: $('input[name=lastName]').val(),
            department: $('input[name=department]').val(),
            salary: $('input[name=salary]').val(),
            age: $('input[name=age]').val(),
            email: $('input[name=email]').val(),
            enabledByte: $('input[name=enabledByte]').val(),
            authorities: $('input[name=authorities]').val()
        };

        await fetch(`/users`, {
            method: 'PUT',
            headers: {
                ...getCsrfHeaders(),
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(user)
        });

    });
});

// disable
$(document).ready(function () {
    $('td a.btn-outline-warning').on('click', async function () {
        let username = $(this).closest('tr').children().eq(0).text();

        await fetch(`/users/${username}`, {
            method: 'PATCH',
            headers: {
                ...getCsrfHeaders(),
                'patch_type' : 'disable'
            }
        });

        $(this).removeClass('btn-outline-warning')
            .addClass('btn-outline-success')
            .text('Enable')
            .parent().prev().text('-');
    });
});

// enable
$(document).ready(function () {
    $('td a.btn-outline-success').on('click', async function () {
        let username = $(this).closest('tr').children().eq(0).text();

        await fetch(`/users/${username}`, {
            method: 'PATCH',
            headers: {
                ...getCsrfHeaders(),
                'patch_type' : 'enable'
            }
        });

        $(this).removeClass('btn-outline-success')
            .addClass('btn-outline-warning')
            .text('Disable')
            .parent().prev().text('+');
    });
});

// удаление юзера
$(document).ready(function () {
    $('.modal-body a.btn-danger').on('click', async function () {
        let row = $(this).closest('tr');
        let username = row.children().eq(0).text();

        await fetch(`/users/${username}`, {
            method: 'DELETE',
            headers: getCsrfHeaders()
        });
        row.remove();
    });
});

function getCsrfHeaders() {
    let csrfToken = $('meta[name="_csrf"]').attr('content');
    let csrfHeaderName = $('meta[name="_csrf_header"]').attr('content');

    let headers = {};
    headers[csrfHeaderName] = csrfToken;
    return headers;
}