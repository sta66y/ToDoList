const btn_add = document.getElementById('add');
const btn_back = document.getElementById('back');
const btn_save = document.getElementById('save');
const btn_saveJSON = document.getElementById('saveJSON');
let taskList = document.getElementById("taskList");

btn_add.addEventListener('click', () => {
    window.location.href = "../pages/addNew.html";
});

async function loadTasks() {
    const response = await fetch("http://localhost:8080/api/todos");
    return await response.json();
}

async function renderTasks() {
    let tasks = await loadTasks();
    taskList.innerHTML = "";

    tasks.forEach((task) => {
        let li = document.createElement("li");
        li.className = "task";

        let span = document.createElement("span");
        span.textContent = task.title;

        let buttons = document.createElement("div");
        buttons.className = "buttons";

        let btn_edit = document.createElement("button");
        btn_edit.textContent = "E";

        let btn_delete = document.createElement("button");
        btn_delete.textContent = "D";

        let checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        checkbox.checked = task.completed;

        buttons.appendChild(btn_edit);
        buttons.appendChild(btn_delete);
        buttons.appendChild(checkbox);
        li.appendChild(span);
        li.appendChild(buttons);
        taskList.appendChild(li);

        btn_delete.addEventListener('click', async () => {
            await fetch(`http://localhost:8080/api/todos/${task.id}`, { method: "DELETE" });
            renderTasks();
        });

        checkbox.addEventListener('click', async () => {
            await fetch(`http://localhost:8080/api/todos/${task.id}`, { method: "PATCH" });
            renderTasks();
        });

        btn_edit.addEventListener('click', () => {
            localStorage.setItem("editTask", JSON.stringify(task));
            window.location.href = "addNew.html";
        });
    });
}

renderTasks();

btn_back.addEventListener('click', () => {
    window.location.href = "../pages/index.html";
});

btn_save.addEventListener('click', () => {
    window.location.href = "../pages/index.html";
});

btn_saveJSON.addEventListener('click', async () => {
    let tasks = await loadTasks();
    const blob = new Blob([JSON.stringify(tasks)], { type: "application/json" });
    const a = document.createElement("a");
    a.href = URL.createObjectURL(blob);
    a.download = "tasks.json";
    a.click();
    URL.revokeObjectURL(a.href);
});
