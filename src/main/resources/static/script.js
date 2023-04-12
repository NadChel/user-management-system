// фильтрация строк таблицы
$(document).ready(function () {
    $("#my-search-input").on('keyup', function () {
        let value = $(this).val().toLowerCase();
        $("#table-body tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});

// апдейт/добавление
$(document).ready(function () {
    $('form').on('submit', async function (event) {
        event.preventDefault();
        console.log('username: ' + $(this).find('input[name=username]').val())
        console.log('authorities: ' + $(this).find('input[name=authorities]').val());

        let user = {
            id: $(this).find('[name=id]').val(),
            username: $(this).find('[name=username]').val(),
            password: $(this).find('[name=password]').val(),
            name: $(this).find('[name=name]').val(),
            lastName: $(this).find('[name=lastName]').val(),
            department: $(this).find('[name=department]').val(),
            salary: $(this).find('[name=salary]').val(),
            age: $(this).find('[name=age]').val(),
            email: $(this).find('[name=email]').val(),
            enabledByte: $(this).find('[name=enabledByte]').val(),
            authorities: JSON.parse($(this).find('[name=authorities]').val())
        };

        let method = $(this).closest('div').attr('id') === 'add-new-user' ? 'POST' : 'PUT';

        let passwordChange = $(this).find('input[name=password]')
            .attr('type') !== 'hidden';

        await fetch(`/users`, {
            method: `${method}`,
            headers: {
                ...getCsrfHeaders(),
                'Content-Type': 'application/json',
                'password_change': `${passwordChange}`
            },
            body: JSON.stringify(user)
        });

        $(this).closest('.modal').modal('hide');
    });
});

// disable
$(document).ready(function () {
    $('tbody').on('click', 'td a.btn-outline-warning', async function () {
        let username = $(this).closest('tr').children().eq(0).text();
        console.log(`username in disable event handler: ${username}`);

        await fetch(`/users/${username}`, {
            method: 'PATCH',
            headers: {
                ...getCsrfHeaders(),
                'patch_type': 'disable'
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
    $('tbody').on('click', 'td a.btn-outline-success', async function () {
        let username = $(this).closest('tr').children().eq(0).text();
        console.log(`username in enable event handler: ${username}`);

        await fetch(`/users/${username}`, {
            method: 'PATCH',
            headers: {
                ...getCsrfHeaders(),
                'patch_type': 'enable'
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