const btn_save = document.getElementById('save');
const btn_cancel = document.getElementById('cancel');

const editData = JSON.parse(localStorage.getItem("editTask"));
if (editData) {
    document.getElementById("title").value = editData.title;
    document.getElementById("description").value = editData.description;
    document.getElementById("status").checked = editData.completed;
}

btn_cancel.addEventListener('click', () => {
    window.location.href = "../pages/menu.html";
});

btn_save.addEventListener('click', async () => {
    const title = document.getElementById("title").value.trim();
    const description = document.getElementById("description").value.trim();
    const status = document.getElementById("status").checked;

    if (title === "") {
        alert("Введите название задачи!");
        return;
    }

    // ✅ объявляем newTask один раз, всегда
    const newTask = { title, description, completed: status };

    if (editData) {
        await fetch(`http://localhost:8080/api/todos/${editData.id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(newTask)
        });
        localStorage.removeItem("editTask");
    } else {
        await fetch("http://localhost:8080/api/todos", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(newTask)
        });
    }

    window.location.href = "../pages/menu.html";
});
