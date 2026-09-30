# Dawn User Guide

**Dawn** is a desktop CLI (Command Line Interface) task management chatbot tailored to help you keep track of your daily tasks, deadlines, and events efficiently. Optimized for quick typing, Dawn allows you to manage your schedule seamlessly through standard text commands.

---

## 🚀 Quick Start

1. Ensure you have **Java 17** or above installed on your computer.
2. Download the latest `dawn.jar` release.
3. Open a command prompt or terminal in the folder where the `.jar` file is located, and type:
   ```bash
   java -jar dawn.jar
   ```
4. You will be greeted by Dawn's welcome banner. Start entering commands to begin managing your day!

---

## 🛠 Features

Here are all the commands you can use to interact with Dawn.

### Managing Tasks

#### Add a ToDo task: `todo`
Adds a standard task without any specific date attached.
**Format:** `todo [description]`
**Example:** `todo read a book`

#### Add a Deadline task: `deadline`
Adds a task that needs to be done by a specific date.
**Format:** `deadline [description] /by [date]`
*Note: The `[date]` can be entered flexibly (e.g., `2026-10-12` or `2026-10-12 1800`)*
**Example:** `deadline submit assignment /by 2026-11-20 2359`

#### Add an Event task: `event`
Adds a task that spans a duration defined by a start time and an end time.
**Format:** `event [description] /from [start date] /to [end date]`
**Example:** `event project meeting /from 2026-10-02 1400 /to 2026-10-02 1600`

---

### Tracking Progress

#### List all tasks: `list`
Displays all the tasks currently saved in your task list along with their indices and completion statuses.
**Format:** `list`

#### Mark a task as done: `mark`
Marks the task at the specified index as completed.
**Format:** `mark [task_number]`
**Example:** `mark 1`

#### Unmark a task: `unmark`
Marks a previously completed task at the specified index as not done yet.
**Format:** `unmark [task_number]`
**Example:** `unmark 1`

---

### Modifying & Searching

#### Delete a task: `delete`
Removes the task at the specified index from your list permanently.
**Format:** `delete [task_number]`
**Example:** `delete 3`

#### Find tasks by keyword: `find`
Retrieves all tasks whose description contains the specified case-insensitive keyword.
**Format:** `find [keyword]`
**Example:** `find meeting`

#### View tasks by date: `view`
Displays all tasks (Deadlines and Events) that occur or fall precisely on the target calendar date.
**Format:** `view [YYYY-MM-DD]`
**Example:** `view 2026-10-02`

---

### Exiting

#### Exit the application: `bye`
Safely terminates the chatbot session.
**Format:** `bye`

---

## 💾 Data Storage

Dawn automatically and seamlessly saves all your tasks locally to your hard disk after any successful modification command (like add, delete, mark, or unmark). 

There is no need to manually save your work. Upon starting Dawn the next time, your previously saved tasks will be automatically loaded into the new session.
