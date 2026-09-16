# Dawn UI test plan

Run these tests from the repository root after compiling with Java 25:

```powershell
$javaSources = @(Get-ChildItem -Path src\main\java -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
javac -d out\production\ip $javaSources
powershell -ExecutionPolicy Bypass -File .codex\skills\test-ui\scripts\run-ui-tests.ps1 -ProgramCommand 'java "-Dstdout.encoding=UTF-8" -cp out/production/ip dawn.Dawn'
```

Each case is a new console session. The listed inputs are entered in order, and the expected output is the complete session transcript. The runner expands the following output tokens before it compares output exactly, apart from platform line endings, final newlines, and trailing padding on a line.

## Output tokens

### INTRO
```text
____________________________________________________________
⬜⬜⬜⬜⬛⬛⬛⬛⬜⬜⬜⬜⬜⬜⬜
⬜⬜⬛⬛🟦🟦🟦🟦⬛⬛⬜⬜⬜⬜⬜
⬜⬛🟦🟦🟦🟦🟦🟦🟦🟦⬛⬜⬜⬜⬜
⬜⬛🟦🟦🟦🟦🟦🟦🟦🟦🟦⬛⬜⬜⬜
⬛🟦🟦🟦🟦🟦🟦🟦🟦🟦🟦⬛⬜⬜⬜
⬛🟦🟦🟦🟦🟦⬜⬜⬜🟦🟦⬛⬜⬜⬜
⬛🟦🟦🟦🟦⬜⬜⬜⬜🟦🟦⬛⬜⬜⬜
⬛🟧🟧🟦⬜⬜⬛⬜⬜🟦🟦⬛⬜⬜⬜
⬜⬛🟧🟧⬜⬜⬛⬜🟦🟦⬛⬜⬜⬜⬜
⬜⬜⬛⬛🟦🟦🟦🟦🟦⬛🟦⬛⬜⬛⬛
⬜⬜⬛🟦⬛⬛⬛⬛⬛🟦🟦🟦⬛🟦⬛
⬜⬜⬜⬛⬛🟦🟦🟦⬛⬛⬛🟦🟦⬛⬜
⬜⬜⬜⬛⬜⬛⬛⬛🟦🟦🟦⬛🟦⬛⬜
⬜⬜⬜⬛🟦🟦⬜⬛🟦🟦🟦⬛⬛⬜⬜
⬜⬜⬛🟧🟦🟦🟦🟦⬛🟦🟦⬛⬜⬜⬜
⬜⬜⬜⬛⬛⬛⬛⬛⬛⬛⬛⬜⬜⬜⬜
⬜⬜⬜⬜⬜⬜⬛🟧🟧⬛⬜⬜⬜⬜⬜
⬜⬜⬜⬜⬜⬜⬜⬛⬛⬜⬜⬜⬜⬜⬜Hello! I'm Dawn.
What can I do for you?
____________________________________________________________
```

### BYE
```text
Bye. Hope to see you again soon!
____________________________________________________________
```

## Test cases

### TC-01: Exit cleanly
**Aim:** Confirm that `bye` ends a newly started session with the farewell message.
**Inputs:**
```text
bye
```
**Expected output:**
```text
${INTRO}

____________________________________________________________

${BYE}
```

### TC-02: Add every task type and list them
**Aim:** Confirm that todo, deadline, event, and list commands show the created task details in insertion order.
**Inputs:**
```text
todo read book
deadline submit report /by Friday
event team meeting /from 2pm /to 3pm
list
bye
```
**Expected output:**
```text
${INTRO}

____________________________________________________________

added: read book

____________________________________________________________

____________________________________________________________

added: submit report

____________________________________________________________

____________________________________________________________

added: team meeting

____________________________________________________________

____________________________________________________________

Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] submit report (by: Friday)
3.[E][ ] team meeting (from: 2pm to: 3pm)
____________________________________________________________

____________________________________________________________

${BYE}
```

### TC-03: Mark and unmark a task
**Aim:** Confirm that task status changes are acknowledged and reflected by `list`.
**Inputs:**
```text
todo revise notes
mark 1
unmark 1
list
bye
```
**Expected output:**
```text
${INTRO}

____________________________________________________________

added: revise notes

____________________________________________________________

____________________________________________________________

Nice! I've marked this task as done:
	[T][X] revise notes

____________________________________________________________

____________________________________________________________

OK, I've marked this task as not done yet:
	[T][ ] revise notes

____________________________________________________________

____________________________________________________________

Here are the tasks in your list:
1.[T][ ] revise notes
____________________________________________________________

____________________________________________________________

${BYE}
```

### TC-04: Reject missing todo details and unavailable task numbers
**Aim:** Confirm that specific validation feedback is shown and that the session continues after each error.
**Inputs:**
```text
todo
mark 1
bye
```
**Expected output:**
```text
${INTRO}

____________________________________________________________

A todo needs a description. Use: todo [description]

____________________________________________________________

____________________________________________________________

Task number not found. Use: mark [task number]

____________________________________________________________

____________________________________________________________

${BYE}
```

### TC-05: Require exact command words and reject the removed add alias
**Aim:** Confirm that malformed task-command words do not create tasks and receive helpful feedback.
**Inputs:**
```text
todoadd read a book
addtodo read a book
add todo read a book
bye
```
**Expected output:**
```text
${INTRO}

____________________________________________________________

Command not recognised. Did you mean: todo [description]?

____________________________________________________________

____________________________________________________________

Command not recognised. Did you mean: todo [description]?

____________________________________________________________

____________________________________________________________

Command not recognised. Supported commands: todo, deadline, event, list, mark, unmark, delete, bye.

____________________________________________________________

____________________________________________________________

${BYE}
```

### TC-09: Delete a task by its displayed number
**Aim:** Confirm that `delete` removes the selected task, reports it on an indented line, and renumbers the remaining list.
**Inputs:**
```text
todo read book
deadline submit report /by Friday
event team meeting /from 2pm /to 3pm
delete 2
list
bye
```
**Expected output:**
```text
${INTRO}

____________________________________________________________

added: read book

____________________________________________________________

____________________________________________________________

added: submit report

____________________________________________________________

____________________________________________________________

added: team meeting

____________________________________________________________

____________________________________________________________

Noted. I've removed this task:
	[D][ ] submit report (by: Friday)
Now you have 2 tasks in the list

____________________________________________________________

____________________________________________________________

Here are the tasks in your list:
1.[T][ ] read book
2.[E][ ] team meeting (from: 2pm to: 3pm)
____________________________________________________________

____________________________________________________________

${BYE}
```

### TC-10: Reject malformed and unavailable delete task numbers
**Aim:** Confirm that `delete` explains its required format and leaves the task list unchanged after invalid requests.
**Inputs:**
```text
todo read book
delete
delete /1
delete 0
delete 2
list
bye
```
**Expected output:**
```text
${INTRO}

____________________________________________________________

added: read book

____________________________________________________________

____________________________________________________________

A task number is required. Use: delete [task number]

____________________________________________________________

____________________________________________________________

The task number must be a positive integer. Use: delete [task number]

____________________________________________________________

____________________________________________________________

The task number must be a positive integer. Use: delete [task number]

____________________________________________________________

____________________________________________________________

Task number not found. Use: delete [task number]

____________________________________________________________

____________________________________________________________

Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________

____________________________________________________________

${BYE}
```

### TC-06: Explain malformed deadline commands
**Aim:** Confirm that deadline errors distinguish missing markers, descriptions, and due dates.
**Inputs:**
```text
deadline watch lecture /by
deadline watch lecture by Friday
deadline /by Friday
deadline watch lecture /by Friday
list
bye
```
**Expected output:**
```text
${INTRO}

____________________________________________________________

The due date cannot be blank. Use: deadline [description] /by [due date]

____________________________________________________________

____________________________________________________________

A deadline needs the /by keyword. Use: deadline [description] /by [due date]

____________________________________________________________

____________________________________________________________

The deadline description cannot be blank. Use: deadline [description] /by [due date]

____________________________________________________________

____________________________________________________________

added: watch lecture

____________________________________________________________

____________________________________________________________

Here are the tasks in your list:
1.[D][ ] watch lecture (by: Friday)
____________________________________________________________

____________________________________________________________

${BYE}
```

### TC-07: Validate task numbers and commands without arguments
**Aim:** Confirm that task numbers must be positive integers and that `list` and `bye` reject arguments.
**Inputs:**
```text
todo read book
mark
unmark
mark abc12
unmark 0
mark 2
list extra
bye now
list
bye
```
**Expected output:**
```text
${INTRO}

____________________________________________________________

added: read book

____________________________________________________________

____________________________________________________________

A task number is required. Use: mark [task number]

____________________________________________________________

____________________________________________________________

A task number is required. Use: unmark [task number]

____________________________________________________________

____________________________________________________________

The task number must be a positive integer. Use: mark [task number]

____________________________________________________________

____________________________________________________________

The task number must be a positive integer. Use: unmark [task number]

____________________________________________________________

____________________________________________________________

Task number not found. Use: mark [task number]

____________________________________________________________

____________________________________________________________

The list command does not accept arguments. Use: list

____________________________________________________________

____________________________________________________________

The bye command does not accept arguments. Use: bye

____________________________________________________________

____________________________________________________________

Here are the tasks in your list:
1.[T][ ] read book
____________________________________________________________

____________________________________________________________

${BYE}
```

### TC-08: Explain malformed event commands
**Aim:** Confirm that event errors identify the missing or misplaced field without adding an invalid task.
**Inputs:**
```text
event /from 10am /to 11am
event meeting /from /to 11am
event meeting /from 10am /to
event meeting from 10am /to 11am
event meeting /to 11am /from 10am
event meeting /from 10am /to 11am
list
bye
```
**Expected output:**
```text
${INTRO}

____________________________________________________________

The event description cannot be blank. Use: event [description] /from [start] /to [end]

____________________________________________________________

____________________________________________________________

The event start cannot be blank. Use: event [description] /from [start] /to [end]

____________________________________________________________

____________________________________________________________

The event end cannot be blank. Use: event [description] /from [start] /to [end]

____________________________________________________________

____________________________________________________________

An event needs the /from keyword. Use: event [description] /from [start] /to [end]

____________________________________________________________

____________________________________________________________

The /to keyword must come after /from. Use: event [description] /from [start] /to [end]

____________________________________________________________

____________________________________________________________

added: meeting

____________________________________________________________

____________________________________________________________

Here are the tasks in your list:
1.[E][ ] meeting (from: 10am to: 11am)
____________________________________________________________

____________________________________________________________

${BYE}
```
