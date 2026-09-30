# Dawn User Guide

## Table of Contents
* [Introduction](#introduction)
* [Quick Start](#quick-start)
* [Features](#features)
  * [Adding a todo task: `todo`](#adding-a-todo-task-todo)
  * [Adding a deadline task: `deadline`](#adding-a-deadline-task-deadline)
  * [Adding an event task: `event`](#adding-an-event-task-event)
  * [Listing all tasks: `list`](#listing-all-tasks-list)
  * [Marking a task as done: `mark`](#marking-a-task-as-done-mark)
  * [Unmarking a task: `unmark`](#unmarking-a-task-unmark)
  * [Deleting a task: `delete`](#deleting-a-task-delete)
  * [Searching for tasks by keyword: `find`](#searching-for-tasks-by-keyword-find)
  * [Viewing tasks by date: `view`](#viewing-tasks-by-date-view)
  * [Exiting the application: `bye`](#exiting-the-application-bye)
* [FAQ](#faq)
* [Command Summary](#command-summary)

---

## Introduction

**Dawn** is a desktop Command Line Interface (CLI) task management chatbot tailored to help you keep track of your daily tasks, deadlines, and events efficiently. 

> [!NOTE]
> Optimized for users who prefer using a keyboard, Dawn allows you to manage your schedule seamlessly through standard text commands.

---

## Quick Start

1. Ensure you have **Java 17** or above installed on your computer.
2. Download the latest `dawn.jar` release from the repository.
3. Open a command prompt or terminal in the folder where the `.jar` file is located.
4. Start the application by running the following command:
   ```bash
   java -jar dawn.jar
   ```
5. You will be greeted by Dawn's welcome banner. Start entering commands to begin managing your day!

---

## Features

> [!IMPORTANT]
> Words in `[brackets]` represent parameters to be supplied by the user.

### Adding a todo task: `todo`

Adds a standard task without any specific date attached to it.

* **Format:** `todo [description]`
* **Examples:**
  * `todo read a book`
  * `todo wash the dishes`

---

### Adding a deadline task: `deadline`

Adds a task that needs to be done by a specific deadline. 

> [!TIP]
> The due date can be flexibly inputted as plain text, or in standard date/time shapes such as `yyyy-mm-dd` (e.g. 2026-10-12) or specific day/month variations (e.g. 12 Oct 2026).

* **Format:** `deadline [description] /by [due date]`
* **Examples:**
  * `deadline submit assignment /by 2026-11-20`
  * `deadline return library book /by tomorrow night`

---

### Adding an event task: `event`

Adds an event task that spans a duration defined by a start time and an end time.

* **Format:** `event [description] /from [start time] /to [end time]`
* **Examples:**
  * `event project meeting /from 2026-10-02 1400 /to 2026-10-02 1600`
  * `event career fair /from Monday 10am /to Wednesday 5pm`

---

### Listing all tasks: `list`

Displays all the tasks currently saved in your task list, alongside their completion statuses.

* **Format:** `list`

---

### Marking a task as done: `mark`

Marks the task at the specified numerical index in the list as completed.

* **Format:** `mark [task number]`
* **Examples:**
  * `mark 1` *(Marks the 1st task as done).*

---

### Unmarking a task: `unmark`

Marks a previously completed task at the specified index as uncompleted.

* **Format:** `unmark [task number]`
* **Examples:**
  * `unmark 1` *(Unmarks the 1st task).*

---

### Deleting a task: `delete`

Permanently deletes the task at the specified index from your tracker.

> [!WARNING]
> This action cannot be undone.

* **Format:** `delete [task number]`
* **Examples:**
  * `delete 3` *(Deletes the 3rd task in the list).*

---

### Searching for tasks by keyword: `find`

Retrieves and displays all tasks whose description contains the specified keyword. This search is case-insensitive.

* **Format:** `find [keyword]`
* **Examples:**
  * `find meeting`
  * `find BOOK`

---

### Viewing tasks by date: `view`

Shows all deadline and event tasks that organically occur on a particular calendar date.

* **Format:** `view [YYYY-MM-DD]`
* **Examples:**
  * `view 2026-10-02`

---

### Exiting the application: `bye`

Safely exits the chatbot interface.

* **Format:** `bye`

---

## FAQ

**Q**: How do I save my data? Do I need to run a save command?  
**A**: There is no manual save command. Dawn **automatically saves** your tasks to the hard disk in the background after any data creation or modification (e.g., adding, marking, or deleting a task). When you start the application again, your data is seamlessly loaded into the session.

**Q**: Where is my data saved?  
**A**: Your data is securely saved in a `dawn.txt` file located in the `data/` folder within the same directory as the `.jar` document.

---

## Command Summary

| Action | Format | Examples |
|--------|--------|----------|
| **Add ToDo** | `todo [description]` | `todo read a book` |
| **Add Deadline** | `deadline [description] /by [due date]` | `deadline submit assignment /by 2026-11-20` |
| **Add Event** | `event [description] /from [start] /to [end]` | `event project meeting /from 1400 /to 1600` |
| **List** | `list` | `list` |
| **Mark** | `mark [task number]` | `mark 1` |
| **Unmark** | `unmark [task number]` | `unmark 1` |
| **Delete** | `delete [task number]` | `delete 3` |
| **Find** | `find [keyword]` | `find meeting` |
| **View** | `view [YYYY-MM-DD]` | `view 2026-10-02` |
| **Exit** | `bye` | `bye` |
