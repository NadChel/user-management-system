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
            authorities: JSON.parse($(this).find('[name=authorities]:checked').val())
        };

        console.log('user: ' + JSON.stringify(user));

        let method = $(this).closest('div').attr('id') === 'add-new-user' ? 'POST' : 'PUT';

        let passwordChange = $(this).find('input[name=password]')
            .attr('type') !== 'hidden';

        const response = await fetch(`/users`, {
            method: `${method}`,
            headers: {
                ...getCsrfHeaders(),
                'Content-Type': 'application/json',
                'password_change': `${passwordChange}`
            },
            body: JSON.stringify(user)
        });

        console.log('response.ok: ' + response.ok);

        if (response.ok) {

            console.log(`add-new-user: ${$(this).closest('div').attr('id') === 'add-new-user'}`);
            console.log(`update-info-modal: ${$(this).closest('.modal').attr('id') === 'update-info-modal'}`);
            console.log(`update-modal-: ${$(this).closest('.modal').attr('id') && 
                                        $(this).closest('.modal').attr('id').startsWith('update-modal-')}`);

            if ($(this).closest('div').attr('id') === 'add-new-user') {
                let usersAuthorities = '';

                console.log(`user.authorities: ${JSON.stringify(user.authorities)}`);
                for (let i = 0; i < user.authorities.length; i++) {
                    console.log(`i: ${i};  user.authorities[i]: ${JSON.stringify(user.authorities[i])}`);
                    if (i !== 0) usersAuthorities += ', ';
                    usersAuthorities += user.authorities[i]['authority'].charAt(0);
                }

                // let buttons = $('td.btn-group').html();
                //
                // buttons.replace(/(?<=(data-target|id)=".+modal-)\w+(?=")/g, user.username)
                //     .replace(/(?<=Edit ).+(?=<)/g, `${user.name} ${user.lastName} (${user.username})`)
                //     .replace(/(?<=If you think ).+(?= personal)/g, `${user.name}'s`);

                $('tbody').append(
                    `<tr>
                         <td>
                             ${user.username}
                         </td>
                         <td>
                             ${user.name}
                         </td>
                         <td>
                             ${user.lastName}
                         </td>
                         <td>
                             ${user.department}
                         </td>                     
                         <td>
                             ${user.salary}
                         </td>                     
                         <td>
                             ${user.age}
                         </td>                     
                         <td>
                             ${user.email}
                         </td>
                         <td>
                             ${usersAuthorities}
                         </td> 
                         <td>
                             ${user.enabledByte !== 0 ? '+' : '-'}
                         </td>
                         <td>
                             <a class="btn btn-primary" href="/">Refresh to see the buttons</a>
                         </td>
                    </tr>`);

                const submitButton = $(this).find('[type=submit]');

                submitButton.removeClass('btn-primary')
                    .addClass('btn-success')
                    .attr('value', 'User added!')
                    .attr('type', 'button')
                    .click(() => $('[href="#users-table"]').tab('show'));

                setTimeout(function () {
                    submitButton.removeClass('btn-success')
                        .addClass('btn-primary')
                        .attr('value', 'Submit')
                        .attr('type', 'submit')
                        .off('click');
                }, 9000);

            } else if ($(this).closest('.modal').attr('id') === 'update-info-modal') {
                const listItems = $(this).closest('.card-body').find('ol').children();

                console.log(`listItems: ${JSON.stringify(listItems)}`);

                listItems.eq(0).find('span').text(user.username);
                listItems.eq(1).find('span').text(user.name);
                listItems.eq(2).find('span').text(user.lastName);
                listItems.eq(5).find('span').text(user.age);
                listItems.eq(6).find('span').text(user.email);
            } else if ($(this).closest('.modal').attr('id').startsWith('update-modal-')) {
                const children = $(this).closest('tr').children();

                console.log('children.eq(0).text(): ' + children.eq(0).text());

                children.eq(0).text(user.username);
                children.eq(1).text(user.name);
                children.eq(2).text(user.lastName);
                children.eq(3).text(user.department);
                children.eq(4).text(user.salary);
                children.eq(5).text(user.age);
                children.eq(6).text(user.email);
            }
        }

        $(this).closest('.modal').modal('hide');
    });
});

// disable
$(document).ready(function () {
    $('tbody').on('click', 'td a.btn-outline-warning', async function () {
        let username = $(this).closest('tr').children().eq(0).text();
        console.log(`username in disable event handler: ${username}`);

        const response = await fetch(`/users/${username}`, {
            method: 'PATCH',
            headers: {
                ...getCsrfHeaders(),
                'patch_type': 'disable'
            }
        });

        if (response.ok) {
            $(this).removeClass('btn-outline-warning')
                .addClass('btn-outline-success')
                .text('Enable')
                .parent().prev().text('-');
        }
    });
});

// enable
$(document).ready(function () {
    $('tbody').on('click', 'td a.btn-outline-success', async function () {
        let username = $(this).closest('tr').children().eq(0).text();
        console.log(`username in enable event handler: ${username}`);

        const response = await fetch(`/users/${username}`, {
            method: 'PATCH',
            headers: {
                ...getCsrfHeaders(),
                'patch_type': 'enable'
            }
        });

        if (response.ok) {
            $(this).removeClass('btn-outline-success')
                .addClass('btn-outline-warning')
                .text('Disable')
                .parent().prev().text('+');
        }
    });
});

// удаление юзера
$(document).ready(function () {
    $('.modal-body a.btn-danger').on('click', async function () {
        let row = $(this).closest('tr');
        let username = row.children().eq(0).text();

        const response = await fetch(`/users/${username}`, {
            method: 'DELETE',
            headers: getCsrfHeaders()
        });

        if (response.ok) {
            row.remove();
        }
    });
});

function getCsrfHeaders() {
    let csrfToken = $('meta[name="_csrf"]').attr('content');
    let csrfHeaderName = $('meta[name="_csrf_header"]').attr('content');

    let headers = {};
    headers[csrfHeaderName] = csrfToken;
    return headers;
}