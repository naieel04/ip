# Dawn UI test plan

Run these tests from the repository root after compiling with Java 25:

```powershell
$javaSources = @(Get-ChildItem -Path src\main\java -Recurse -Filter *.java | Select-Object -ExpandProperty FullName)
javac -d out\production\ip $javaSources
powershell -ExecutionPolicy Bypass -File .gemini\skills\test-ui\scripts\run-ui-tests.ps1 -ProgramCommand 'java "-Dstdout.encoding=UTF-8" -cp out/production/ip dawn.Dawn'
```

Each case is a new console session. The listed inputs are entered in order, and the expected output is the complete session transcript. The runner expands the following output tokens before it compares output exactly, apart from platform line endings, final newlines, and trailing padding on a line.

## Output tokens

### INTRO
```text
____________________________________________________________

                             ..#+++.-+++##+.                    
                         .#...##..............+-                
                      .+.....##+....................            
                    ..................................          
                  -............................   ..            
                ..     ...................... +##########       
               .########. .......  ........ -#############+     
              ###########+  ... .##. ... . -################    
             ###### #######.#. ###### .##-.#######. #########   
            ######  . #####.+#####+###### #####. ..   #######.  
            #####  +###+####.#- +###+..#++##### ####   #######  
           -####.  +###+####  #########  #####- ####   -######  
           +####.   ##+ #### ########### #####.   .    .######. 
           +####.       ####.##########..#####.        +######. 
   ##      .####.      .####.-#######-.#.######        #######. 
  ####      #####      #####+-#+---+###.########      ########  
##+#+###    ######+   ######## +###### ######################.  
  ##+#       ####################...#########################   
    #         ##############################################    
              .###########################################+     
                #########################################       
                 .######################-       .######         
                   .####-  .....####-.+++-......-.. .           
                      .#+........+.+#.................###.      
                ###.+#................................######.   
            +#####.++.............  ............... .##..#####. 
          .####### #............. #- ............ .##..+####### 
          ########+.#.......... .####+   ....   +##+ +#########. 
         .########+   ......  .########+.#####.###+ ########### 
          #########-.## ..-##+.#######.+######++##..##########  
          .######### #+-###### ####### ########.##..########.   
            .######+ ##.###### ######## ######.#### ######.     
              .####.-##+.#### ##########- .- .###### .###.      
              .++. #######++##########################.         
                   ###################################...       
                   -################################## .        
                    #################################.          
                     ###############################.           
                      #############################             
                      . ########################+..+.           
                   -###-.##.                 .+##.-###.         
                 .########+++                -+++#######        
               .-.+#######+.                  .########+#.      
               -#+.+###+.                       .+####+#+..     
                 .-                                 +##..#+     

Hello! I'm Dawn.
What can I do for you?

Here are the commands you can use:
  todo [description]                     - Add a todo task
  deadline [description] /by [due date]  - Add a deadline task
  event [desc] /from [start] /to [end]   - Add an event task
  list                                   - List all tasks
  mark [task number]                     - Mark a task as done
  unmark [task number]                   - Mark a task as not done
  delete [task number]                   - Delete a task
  view [date]                            - View tasks on a specific date
  bye                                    - Exit the application
____________________________________________________________
```

### BYE
```text
Bye. Hope to see you again soon!
____________________________________________________________
```

### LINE
```text
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

${LINE}

${BYE}
```

### TC-02: Add every task type and list them
**Aim:** Confirm that todo, deadline, event, and list commands show the created task details in insertion order.
**Inputs:**
```text
todo read book
deadline submit report /by 2019-10-15
event team meeting /from 2pm /to 3pm
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

added: read book

${LINE}

${LINE}

added: submit report

${LINE}

${LINE}

added: team meeting

${LINE}

${LINE}

Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] submit report (by: 15 Oct 2019)
3.[E][ ] team meeting (from: 2pm to: 3pm)
${LINE}

${LINE}

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

${LINE}

added: revise notes

${LINE}

${LINE}

Nice! I've marked this task as done:
	[T][X] revise notes

${LINE}

${LINE}

OK, I've marked this task as not done yet:
	[T][ ] revise notes

${LINE}

${LINE}

Here are the tasks in your list:
1.[T][ ] revise notes
${LINE}

${LINE}

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

${LINE}

A todo needs a description. Use: todo [description]

${LINE}

${LINE}

Task number not found. Use: mark [task number]

${LINE}

${LINE}

${BYE}
```

### TC-05: Require exact command words and suggest intended commands
**Aim:** Confirm that unrecognized command words suggest matching command syntax or display the supported command list.
**Inputs:**
```text
todoadd read a book
addtodo read a book
deadlineurgent submit /by Friday
eventnow camp /from 2pm /to 4pm
marking 1
unmarking 1
deleteitem 1
viewtasks 2019-10-15
schedules 2019-10-15
add todo read a book
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Command not recognised.
Did you mean: todo [description]?

${LINE}

${LINE}

Command not recognised.
Did you mean: todo [description]?

${LINE}

${LINE}

Command not recognised.
Did you mean: deadline [description] /by [due date]?

${LINE}

${LINE}

Command not recognised.
Did you mean: event [description] /from [start] /to [end]?

${LINE}

${LINE}

Command not recognised.
Did you mean: mark [task number]?

${LINE}

${LINE}

Command not recognised.
Did you mean: unmark [task number]?

${LINE}

${LINE}

Command not recognised.
Did you mean: delete [task number]?

${LINE}

${LINE}

Command not recognised.
Did you mean: view [date]?

${LINE}

${LINE}

Command not recognised.
Did you mean: view [date]?

${LINE}

${LINE}

Command not recognised.
Supported commands:
  - todo, deadline, event
  - list, mark, unmark, delete
  - view, bye

${LINE}

${LINE}

${BYE}
```

### TC-06: Explain malformed deadline commands
**Aim:** Explain malformed deadline commands, including missing /by, missing description, missing due date, and freeform date strings.
**Inputs:**
```text
deadline watch lecture /by
deadline watch lecture by 2019-10-15
deadline /by 2019-10-15
deadline watch lecture /by Friday
deadline watch lecture /by 2019-10-15
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

The due date cannot be blank. Use: deadline [description] /by [due date]

${LINE}

${LINE}

A deadline needs the /by keyword. Use: deadline [description] /by [due date]

${LINE}

${LINE}

The deadline description cannot be blank. Use: deadline [description] /by [due date]

${LINE}

${LINE}

added: watch lecture

${LINE}

${LINE}

added: watch lecture

${LINE}

${LINE}

Here are the tasks in your list:
1.[D][ ] watch lecture (by: Friday)
2.[D][ ] watch lecture (by: 15 Oct 2019)
${LINE}

${LINE}

${BYE}
```

### TC-07: Validate task numbers and commands without arguments
**Aim:** Confirm that task numbers must be positive integers without overflowing, and that `list` and `bye` reject arguments.
**Inputs:**
```text
todo read book
mark
unmark
delete
mark abc12
unmark 0
delete -1
delete 0
delete abc
mark 99999999999999999999
unmark 99999999999999999999
delete 99999999999999999999
mark 2
delete 2
list extra
bye now
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

added: read book

${LINE}

${LINE}

A task number is required. Use: mark [task number]

${LINE}

${LINE}

A task number is required. Use: unmark [task number]

${LINE}

${LINE}

A task number is required. Use: delete [task number]

${LINE}

${LINE}

The task number must be a positive integer. Use: mark [task number]

${LINE}

${LINE}

The task number must be a positive integer. Use: unmark [task number]

${LINE}

${LINE}

The task number must be a positive integer. Use: delete [task number]

${LINE}

${LINE}

The task number must be a positive integer. Use: delete [task number]

${LINE}

${LINE}

The task number must be a positive integer. Use: delete [task number]

${LINE}

${LINE}

The task number must be a positive integer. Use: mark [task number]

${LINE}

${LINE}

The task number must be a positive integer. Use: unmark [task number]

${LINE}

${LINE}

The task number must be a positive integer. Use: delete [task number]

${LINE}

${LINE}

Task number not found. Use: mark [task number]

${LINE}

${LINE}

Task number not found. Use: delete [task number]

${LINE}

${LINE}

The list command does not accept arguments. Use: list

${LINE}

${LINE}

The bye command does not accept arguments. Use: bye

${LINE}

${LINE}

Here are the tasks in your list:
1.[T][ ] read book
${LINE}

${LINE}

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
event meeting /from 10am
event meeting /to 11am /from 10am
event meeting /from 10am /to 11am
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

The event description cannot be blank. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

The event start cannot be blank. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

The event end cannot be blank. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

An event needs the /from keyword. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

An event needs the /to keyword. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

The /to keyword must come after /from. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

added: meeting

${LINE}

${LINE}

Here are the tasks in your list:
1.[E][ ] meeting (from: 10am to: 11am)
${LINE}

${LINE}

${BYE}
```

### TC-09: Load tasks from persistent storage and skip corrupted lines
**Aim:** Confirm that Dawn loads saved tasks from data/dawn.txt, handles various corrupted line formats with warnings, and displays valid tasks.
**File input:**
```text
T | 1 | revise notes
corrupted line without delimiters
D | 0 | submit report
E | 1 | project sync | 2pm
X | 0 | unknown type
E | 0 | hackathon | Friday 6pm | Sunday 6pm
```
**Inputs:**
```text
list
bye
```
**Expected output:**
```text
Warning: Corrupted task line skipped: [corrupted line without delimiters] - Missing essential task components.
Warning: Corrupted task line skipped: [D | 0 | submit report] - Deadline is missing the due date.
Warning: Corrupted task line skipped: [E | 1 | project sync | 2pm] - Event is missing start or end dates.
Warning: Corrupted task line skipped: [X | 0 | unknown type] - Unknown task type identifier: X
${INTRO}

${LINE}

Here are the tasks in your list:
1.[T][X] revise notes
2.[E][ ] hackathon (from: Friday 6pm to: Sunday 6pm)
${LINE}

${LINE}

${BYE}
```

### TC-10: Delete a task loaded from storage
**Aim:** Confirm that delete removes the selected stored task and keeps the remaining task correctly numbered.
**File input:**
```text
T | 0 | read book
D | 1 | submit report | 2019-12-02 1800
```
**Inputs:**
```text
delete 1
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Noted. I've removed this task:
	[T][ ] read book
Now you have 1 tasks in the list

${LINE}

${LINE}

Here are the tasks in your list:
1.[D][X] submit report (by: 02 Dec 2019 18:00)
${LINE}

${LINE}

${BYE}
```

### TC-11: Empty list display, blank input handling, and whitespace padding
**Aim:** Confirm that list on an empty list shows the header cleanly, blank lines display supported commands, and whitespace around commands is trimmed.
**Inputs:**
```text
list

   
todo buy groceries
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Here are the tasks in your list:
${LINE}

${LINE}

Command not recognised.
Supported commands:
  - todo, deadline, event
  - list, mark, unmark, delete
  - view, bye

${LINE}

${LINE}

Command not recognised.
Supported commands:
  - todo, deadline, event
  - list, mark, unmark, delete
  - view, bye

${LINE}

${LINE}

added: buy groceries

${LINE}

${LINE}

Here are the tasks in your list:
1.[T][ ] buy groceries
${LINE}

${LINE}

${BYE}
```

### TC-12: Delete all tasks until list is empty and deletion boundaries
**Aim:** Confirm that deleting all tasks updates the count down to 0, subsequent list displays an empty list, and deleting from an empty list reports that the task number was not found.
**Inputs:**
```text
todo temporary task
delete 1
list
delete 1
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

added: temporary task

${LINE}

${LINE}

Noted. I've removed this task:
	[T][ ] temporary task
Now you have 0 tasks in the list

${LINE}

${LINE}

Here are the tasks in your list:
${LINE}

${LINE}

Task number not found. Use: delete [task number]

${LINE}

${LINE}

${BYE}
```

### TC-13: Standalone marker boundary validation for deadline and event
**Aim:** Confirm that /by, /from, and /to must have whitespace boundaries to be recognized as markers, and multiple /by markers in the description/date are handled deterministically.
**Inputs:**
```text
deadline submit work/by tomorrow
deadline submit work /bytomorrow
event camp/from Monday /to Tuesday
event camp /from Monday/to Tuesday
deadline read book by/author /by 2019-10-15
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

A deadline needs the /by keyword. Use: deadline [description] /by [due date]

${LINE}

${LINE}

A deadline needs the /by keyword. Use: deadline [description] /by [due date]

${LINE}

${LINE}

An event needs the /from keyword. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

An event needs the /to keyword. Use: event [description] /from [start] /to [end]

${LINE}

${LINE}

added: read book by/author

${LINE}

${LINE}

Here are the tasks in your list:
1.[D][ ] read book by/author (by: 15 Oct 2019)
${LINE}

${LINE}

${BYE}
```

### TC-14: Mark and unmark idempotency
**Aim:** Confirm that repeatedly marking a completed task or unmarking an incomplete task preserves the status and outputs feedback without error.
**Inputs:**
```text
todo practice coding
mark 1
mark 1
unmark 1
unmark 1
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

added: practice coding

${LINE}

${LINE}

Nice! I've marked this task as done:
	[T][X] practice coding

${LINE}

${LINE}

Nice! I've marked this task as done:
	[T][X] practice coding

${LINE}

${LINE}

OK, I've marked this task as not done yet:
	[T][ ] practice coding

${LINE}

${LINE}

OK, I've marked this task as not done yet:
	[T][ ] practice coding

${LINE}

${LINE}

Here are the tasks in your list:
1.[T][ ] practice coding
${LINE}

${LINE}

${BYE}
```

### TC-15: Maximum task list capacity boundary
**Aim:** Confirm that TaskList rejects additions when the 100-task limit is reached, and allows new tasks after deleting an item.
**File input:**
```text
T | 0 | task 1
T | 0 | task 2
T | 0 | task 3
T | 0 | task 4
T | 0 | task 5
T | 0 | task 6
T | 0 | task 7
T | 0 | task 8
T | 0 | task 9
T | 0 | task 10
T | 0 | task 11
T | 0 | task 12
T | 0 | task 13
T | 0 | task 14
T | 0 | task 15
T | 0 | task 16
T | 0 | task 17
T | 0 | task 18
T | 0 | task 19
T | 0 | task 20
T | 0 | task 21
T | 0 | task 22
T | 0 | task 23
T | 0 | task 24
T | 0 | task 25
T | 0 | task 26
T | 0 | task 27
T | 0 | task 28
T | 0 | task 29
T | 0 | task 30
T | 0 | task 31
T | 0 | task 32
T | 0 | task 33
T | 0 | task 34
T | 0 | task 35
T | 0 | task 36
T | 0 | task 37
T | 0 | task 38
T | 0 | task 39
T | 0 | task 40
T | 0 | task 41
T | 0 | task 42
T | 0 | task 43
T | 0 | task 44
T | 0 | task 45
T | 0 | task 46
T | 0 | task 47
T | 0 | task 48
T | 0 | task 49
T | 0 | task 50
T | 0 | task 51
T | 0 | task 52
T | 0 | task 53
T | 0 | task 54
T | 0 | task 55
T | 0 | task 56
T | 0 | task 57
T | 0 | task 58
T | 0 | task 59
T | 0 | task 60
T | 0 | task 61
T | 0 | task 62
T | 0 | task 63
T | 0 | task 64
T | 0 | task 65
T | 0 | task 66
T | 0 | task 67
T | 0 | task 68
T | 0 | task 69
T | 0 | task 70
T | 0 | task 71
T | 0 | task 72
T | 0 | task 73
T | 0 | task 74
T | 0 | task 75
T | 0 | task 76
T | 0 | task 77
T | 0 | task 78
T | 0 | task 79
T | 0 | task 80
T | 0 | task 81
T | 0 | task 82
T | 0 | task 83
T | 0 | task 84
T | 0 | task 85
T | 0 | task 86
T | 0 | task 87
T | 0 | task 88
T | 0 | task 89
T | 0 | task 90
T | 0 | task 91
T | 0 | task 92
T | 0 | task 93
T | 0 | task 94
T | 0 | task 95
T | 0 | task 96
T | 0 | task 97
T | 0 | task 98
T | 0 | task 99
T | 0 | task 100
```
**Inputs:**
```text
todo task 101
delete 100
todo replacement task
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Dawn can store at most 100 tasks.

${LINE}

${LINE}

Noted. I've removed this task:
	[T][ ] task 100
Now you have 99 tasks in the list

${LINE}

${LINE}

added: replacement task

${LINE}

${LINE}

${BYE}
```

### TC-16: Strict date-time validation for deadlines (leap year, midnight, noon, and display formatting)
**Aim:** Confirm that valid date-time formats (including leap day, midnight, noon, and standard formats) are accepted and displayed cleanly.
**Inputs:**
```text
deadline finish homework /by 2/12/2019 0000
deadline noon meeting /by 2/12/2019 1200
deadline leap day sprint /by 29/02/2020 0900
deadline standard date /by 2019-10-15
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

added: finish homework

${LINE}

${LINE}

added: noon meeting

${LINE}

${LINE}

added: leap day sprint

${LINE}

${LINE}

added: standard date

${LINE}

${LINE}

Here are the tasks in your list:
1.[D][ ] finish homework (by: 02 Dec 2019 00:00)
2.[D][ ] noon meeting (by: 02 Dec 2019 12:00)
3.[D][ ] leap day sprint (by: 29 Feb 2020 09:00)
4.[D][ ] standard date (by: 15 Oct 2019)
${LINE}

${LINE}

${BYE}
```

### TC-17: Rejection of impossible calendar dates and out-of-range time values
**Aim:** Confirm that non-leap years, impossible days, invalid months, hours, minutes, and unsupported text formats are strictly rejected.
**Inputs:**
```text
deadline bad leap /by 29/02/2019 0900
deadline feb 31 /by 31/02/2019 1800
deadline month 13 /by 2019-13-02 1800
deadline day 32 /by 32/01/2019 1800
deadline hour 25 /by 2/12/2019 2500
deadline minute 60 /by 2/12/2019 1860
deadline feb 30 /by 2019-02-30
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

${BYE}
```

### TC-18: Storage persistence round-trip and corrupted date handling for deadlines
**Aim:** Confirm that deadlines with valid dates are loaded and serialized losslessly, while stored deadlines with malformed dates are skipped with warnings.
**File input:**
```text
D | 0 | project submission | 2019-12-02 1800
D | 0 | corrupted deadline | 2019-13-02 1800
D | 1 | day only deadline | 2019-10-15
```
**Inputs:**
```text
list
bye
```
**Expected output:**
```text
Warning: Corrupted task line skipped: [D | 0 | corrupted deadline | 2019-13-02 1800] - Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)
${INTRO}

${LINE}

Here are the tasks in your list:
1.[D][ ] project submission (by: 02 Dec 2019 18:00)
2.[D][X] day only deadline (by: 15 Oct 2019)
${LINE}

${LINE}

${BYE}
```

### TC-19: View command for date filtering
**Aim:** Confirm that view filters tasks occurring on a specific date, informs the user when a time is supplied, handles dates with no tasks, rejects blank arguments, and validates date formats.
**Inputs:**
```text
deadline assignment /by 2019-12-02 1800
deadline project /by 2/12/2019 2359
deadline other day /by 2019-12-03
event conference /from 2019-12-01 /to 2019-12-03
view 2019-12-02
view 2019-12-02 1800
view 2019-12-04
view
view invalid-date
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

added: assignment

${LINE}

${LINE}

added: project

${LINE}

${LINE}

added: other day

${LINE}

${LINE}

added: conference

${LINE}

${LINE}

Here are the tasks occurring on 02 Dec 2019:
1.[D][ ] assignment (by: 02 Dec 2019 18:00)
2.[D][ ] project (by: 02 Dec 2019 23:59)
3.[E][ ] conference (from: 01 Dec 2019 to: 03 Dec 2019)
${LINE}

${LINE}

Note: 'view' queries tasks for the entire day (02 Dec 2019).

Here are the tasks occurring on 02 Dec 2019:
1.[D][ ] assignment (by: 02 Dec 2019 18:00)
2.[D][ ] project (by: 02 Dec 2019 23:59)
3.[E][ ] conference (from: 01 Dec 2019 to: 03 Dec 2019)
${LINE}

${LINE}

No tasks occurring on 04 Dec 2019.

${LINE}

${LINE}

A date is required. Use: view [date]

${LINE}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

${BYE}
```

### TC-20: Event date validation, formatting, and impossible dates rejection
**Aim:** Confirm that events format valid dates and times consistently, accept freeform descriptions, and reject impossible dates or invalid time components without creating tasks.
**Inputs:**
```text
event retreat /from 2026-10-01 /to 2026-10-05
event final exam /from 2/12/2019 0900 /to 2/12/2019 1100
event orientation /from Monday /to Wednesday
event bad start /from 2019-13-01 /to 2019-10-05
event feb 30 /from 2019-02-30 /to 2019-03-01
event bad leap /from 29/02/2019 0900 /to 01/03/2019 0900
event bad hour /from 2/12/2019 2500 /to 2/12/2019 2600
event bad minute /from 2/12/2019 0960 /to 2/12/2019 1100
list
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

added: retreat

${LINE}

${LINE}

added: final exam

${LINE}

${LINE}

added: orientation

${LINE}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)

${LINE}

${LINE}

Here are the tasks in your list:
1.[E][ ] retreat (from: 01 Oct 2026 to: 05 Oct 2026)
2.[E][ ] final exam (from: 02 Dec 2019 09:00 to: 02 Dec 2019 11:00)
3.[E][ ] orientation (from: Monday to: Wednesday)
${LINE}

${LINE}

${BYE}
```

### TC-21: Multi-case corrupted storage recovery and valid task preservation
**Aim:** Verify that storage loading skips various corrupted task lines with descriptive warnings, successfully loads valid tasks across types, and supports further mutations.
**File input:**
```text
T | 1
D | 0 | submit paper
E | 0 | conference | 2026-10-01
E | 0 | hackathon | 2019-13-02 | 2019-13-05
Z | 0 | alien task
T | 0 | read book
D | 1 | submit assignment | 2026-10-02 1400
E | 0 | workshop | 2026-10-01 | 2026-10-03
```
**Inputs:**
```text
list
mark 1
delete 3
list
bye
```
**Expected output:**
```text
Warning: Corrupted task line skipped: [T | 1] - Missing essential task components.
Warning: Corrupted task line skipped: [D | 0 | submit paper] - Deadline is missing the due date.
Warning: Corrupted task line skipped: [E | 0 | conference | 2026-10-01] - Event is missing start or end dates.
Warning: Corrupted task line skipped: [E | 0 | hackathon | 2019-13-02 | 2019-13-05] - Invalid date or time format. Use: yyyy-MM-dd [HHmm] or d/M/yyyy [HHmm] (e.g., 2019-12-02 1800 or 2/12/2019 1800)
Warning: Corrupted task line skipped: [Z | 0 | alien task] - Unknown task type identifier: Z
${INTRO}

${LINE}

Here are the tasks in your list:
1.[T][ ] read book
2.[D][X] submit assignment (by: 02 Oct 2026 14:00)
3.[E][ ] workshop (from: 01 Oct 2026 to: 03 Oct 2026)
${LINE}

${LINE}

Nice! I've marked this task as done:
	[T][X] read book

${LINE}

${LINE}

Noted. I've removed this task:
	[E][ ] workshop (from: 01 Oct 2026 to: 03 Oct 2026)
Now you have 2 tasks in the list

${LINE}

${LINE}

Here are the tasks in your list:
1.[T][X] read book
2.[D][X] submit assignment (by: 02 Oct 2026 14:00)
${LINE}

${LINE}

${BYE}
```

### TC-22: Task list deletion boundaries across positions and sequential re-indexing
**Aim:** Verify that deleting tasks from the middle, front, and end of the list properly re-indexes remaining tasks, and confirm that out-of-range and non-integer deletion indices are rejected.
**Inputs:**
```text
todo first task
todo second task
todo third task
todo fourth task
delete 2
list
delete 1
list
delete 2
list
delete 0
delete -2
delete 2
delete abc
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

added: first task

${LINE}

${LINE}

added: second task

${LINE}

${LINE}

added: third task

${LINE}

${LINE}

added: fourth task

${LINE}

${LINE}

Noted. I've removed this task:
	[T][ ] second task
Now you have 3 tasks in the list

${LINE}

${LINE}

Here are the tasks in your list:
1.[T][ ] first task
2.[T][ ] third task
3.[T][ ] fourth task
${LINE}

${LINE}

Noted. I've removed this task:
	[T][ ] first task
Now you have 2 tasks in the list

${LINE}

${LINE}

Here are the tasks in your list:
1.[T][ ] third task
2.[T][ ] fourth task
${LINE}

${LINE}

Noted. I've removed this task:
	[T][ ] fourth task
Now you have 1 tasks in the list

${LINE}

${LINE}

Here are the tasks in your list:
1.[T][ ] third task
${LINE}

${LINE}

The task number must be a positive integer. Use: delete [task number]

${LINE}

${LINE}

The task number must be a positive integer. Use: delete [task number]

${LINE}

${LINE}

Task number not found. Use: delete [task number]

${LINE}

${LINE}

The task number must be a positive integer. Use: delete [task number]

${LINE}

${LINE}

${BYE}
```

### TC-23: Multi-day event queries with view and schedule alias on populated and empty lists
**Aim:** Verify that date queries correctly detect events on start, middle, and end days, handle empty lists, support the schedule command alias, and display the informational note when time is passed.
**Inputs:**
```text
view 2026-10-02
todo untimed task
deadline urgent /by 2026-10-02 1800
event conference /from 2026-10-01 /to 2026-10-03
deadline freeform /by tonight
view 2026-10-01
schedule 2026-10-02
view 2026-10-03
view 2026-10-04
view 2026-10-02 0900
bye
```
**Expected output:**
```text
${INTRO}

${LINE}

No tasks occurring on 02 Oct 2026.

${LINE}

${LINE}

added: untimed task

${LINE}

${LINE}

added: urgent

${LINE}

${LINE}

added: conference

${LINE}

${LINE}

added: freeform

${LINE}

${LINE}

Here are the tasks occurring on 01 Oct 2026:
1.[E][ ] conference (from: 01 Oct 2026 to: 03 Oct 2026)
${LINE}

${LINE}

Here are the tasks occurring on 02 Oct 2026:
1.[D][ ] urgent (by: 02 Oct 2026 18:00)
2.[E][ ] conference (from: 01 Oct 2026 to: 03 Oct 2026)
${LINE}

${LINE}

Here are the tasks occurring on 03 Oct 2026:
1.[E][ ] conference (from: 01 Oct 2026 to: 03 Oct 2026)
${LINE}

${LINE}

No tasks occurring on 04 Oct 2026.

${LINE}

${LINE}

Note: 'view' queries tasks for the entire day (02 Oct 2026).

Here are the tasks occurring on 02 Oct 2026:
1.[D][ ] urgent (by: 02 Oct 2026 18:00)
2.[E][ ] conference (from: 01 Oct 2026 to: 03 Oct 2026)
${LINE}

${LINE}

${BYE}
```
