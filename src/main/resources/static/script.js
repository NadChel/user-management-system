// фильтрация строк таблицы
$(document).ready(function () {
    $("#my-search-input").on('keyup', function () {
        let value = $(this).val().toLowerCase();
        $("#table-body tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
});

// add/update
$(document).ready(function () {
    $('body').on('submit', 'form', async function (event) {
        event.preventDefault();
        console.log('username: ' + $(this).find('input[name=username]').val())
        console.log('authorities of $(this): ' + $(this).find('input[name=authorities]').val());
        console.log('authorities of $(event.currentTarget): ' + $(event.currentTarget).find('input[name=authorities]').val());
        console.log('authorities of $(event.target): ' + $(event.target).find('input[name=authorities]').val());

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
            authorities: JSON.parse($(this).find('[name=authorities]:checked').val() ||
                $(this).find('[name=authorities]').val())
        };

        console.log('user: ' + JSON.stringify(user));

        let method = $(this).closest('div').attr('id') === 'add-new-user' ? 'POST' : 'PUT';

        let passwordChange = $(this).find('input[name=password]')
            .attr('type') !== 'hidden';

        const response = await fetch('/users', {
            method: `${method}`,
            headers: {
                ...getCsrfHeaders(),
                'Content-Type': 'application/json',
                'password_change': `${passwordChange}`
            },
            body: JSON.stringify(user)
        });

        const data = await response.json();

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
                    if (i !== 0) usersAuthorities += ' ';
                    usersAuthorities += user.authorities[i]['authority'].charAt(0);
                }

                $('tbody').append(
                    `<tr>
                         <td>${user.username}</td>
                         <td>${user.name}</td>
                         <td>${user.lastName}</td>
                         <td>${user.department}</td>                     
                         <td>${user.salary}</td>                     
                         <td>${user.age}</td>                     
                         <td>${user.email}</td>
                         <td>${usersAuthorities}</td> 
                         <td>+</td>
                         <td class="btn-group btn-group-sm">
                            <a class="btn btn-outline-primary" data-toggle="modal"
                               data-target="${'#update-modal-' + user.username}">Update
                            </a>
                            <div class="modal" id="${'update-modal-' + user.username}">
                                <div class="modal-dialog modal-lg modal-dialog-centered">
                                    <div class="modal-content">

                                        <div class="modal-header">
                                            <h3 class="modal-title text-center w-100">
                                                ${'Edit ' + user.name + ' ' + user.lastName + ' (' + user.username + ')'}
                                            </h3>
                                            <button type="button" class="close" data-dismiss="modal">&times;
                                            </button>
                                        </div>

                                        <div class="modal-body text-left">
                                            <p class="text-left">
                                                For the sake of privacy, you are only allowed to change the user's department
                                                and salary. If you think ${user.name}'s personal info needs to be altered, please request them to do so
                                            </p>

                                            <form>
                                                <input type="hidden" name="id" value="${data.id}">

                                                <input type="hidden" name="username"
                                                       value="${user.username}">

                                                <input type="hidden" name="password"
                                                       value="${data.password}">

                                                <input type="hidden" name="name" value="${user.name}">

                                                <input type="hidden" name="lastName"
                                                       value="${user.lastName}">

                                                <div class="form-group">
                                                    <label for="${user.username + '-department'}">Department: </label>
                                                    <select id="${user.username + '-department'}"
                                                            class="form-control" name="department">
                                                        <option ${user.department === 'accounting' ? 'selected' : ''}
                                                                value="accounting">Accounting
                                                        </option>
                                                        <option ${user.department === 'sales' ? 'selected' : ''}
                                                                value="sales">Sales
                                                        </option>
                                                        <option ${user.department === 'information technology' ? 'selected' : ''}
                                                                value="information technology">IT
                                                        </option>
                                                        <option ${user.department === 'human resources' ? 'selected' : ''}
                                                                value="human resources">HR
                                                        </option>
                                                        <option ${user.department === 'board of directors' ? 'selected' : ''}
                                                                value="board of directors">Board
                                                        </option>
                                                    </select>
                                                </div>

                                                <div class="form-group">
                                                    <label for="${user.username + '-salary'}">Salary: </label>
                                                    <input id="${user.username + '-salary'}"
                                                           class="form-control" name="salary"
                                                           value="${user.salary}"
                                                           min="100000" aria-describedby="au-salary-help-block"
                                                           required/>
                                                    <small id="au-salary-help-block"
                                                           class="form-text text-muted">
                                                        100,000+
                                                    </small>
                                                </div>

                                                <input type="hidden" name="age" value="${user.age}">

                                                <input type="hidden" name="email" value="${user.email}">

                                                <input type="hidden" name="enabledByte"
                                                       value="${user.enabledByte}">

                                                <input type="hidden" name="authorities"
                                                       value='${JSON.stringify(user.authorities)}'/>

                                                <input class="btn btn-primary d-flex ml-auto" type="submit"
                                                       value="Submit">
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <a class="btn mx-1 btn-outline-warning">Disable</a>

                            <a class="btn btn-outline-danger" data-toggle="modal"
                               data-target="${'#delete-modal-' + user.username}">Delete
                            </a>

                            <div class="modal" id="${'delete-modal-' + user.username}">
                                <div class="modal-dialog">
                                    <div class="modal-content">

                                        <div class="modal-header">
                                            <h3 class="modal-title text-center w-100">
                                                Are you sure you want to delete ${user.name} ${user.lastName} (${user.username})?
                                            </h3>
                                            <button type="button" class="close" data-dismiss="modal">&times;
                                            </button>
                                        </div>

                                        <div class="modal-body">
                                            <p>This action may not be easily reversed</p>

                                            <a class="btn btn-danger d-flex ml-auto" data-dismiss="modal">I am sure
                                            </a>
                                        </div>

                                    </div>
                                </div>
                            </div>
                         </td>
                    </tr>`);

                const submitButton = $(this).find('[type=submit]');

                const form = $(this);

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
                    form.trigger('reset');
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

// delete
$(document).ready(function () {
    $('tbody').on('click', '.modal-body a.btn-danger', async function () {
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