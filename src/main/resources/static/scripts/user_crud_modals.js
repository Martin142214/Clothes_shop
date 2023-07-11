function openUserEditModal(id, username, email) {
    document.getElementById('id04').style.display='block';
    document.getElementById('username').value = username;
    document.getElementById('email').value = email;
    document.getElementById('modalUserEditForm').setAttribute("action", "/user/" + id + "/edit");
}

function openUserDeleteModal(id, username){
    document.getElementById('id05').style.display='block';
    document.getElementById('h2').innerHTML = "Are you sure you want to delete User: " + username;
    document.getElementById('modalUserDeleteForm').setAttribute("action", "/user/" + id + "/delete");
}