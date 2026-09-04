# Dawn UI test plan

Run these tests from the repository root after compiling with Java 25:

```powershell
javac -d out\production\ip src\main\java\*.java
powershell -ExecutionPolicy Bypass -File .codex\skills\test-ui\scripts\run-ui-tests.ps1
```

Each case is a new console session. The listed inputs are entered in order, and the expected output is the complete session transcript. The runner expands the following output tokens before it compares output exactly, apart from platform line endings, final newlines, and trailing padding on a line.

## Output tokens

### INTRO
```text
____________________________________________________________
                         :::xxxxxxxxxxx;:::.
                   .xxxx+;:..............:;xx;
                ;xx+;::::.....................;x+
             .xx+;:;++++;:.................;++++xxx:
            .x+:;+;.     .;;.............::        .xx
           +x;:+:          .;..........:;.           .x+
          x+::x..+.         ;:.........;:        .xX:  xx
         x+;:+..$$X         ;;.........;.        .X$;  .x;
        ;x;:;+  .:          ;;.........+.               +;
       .x;::;;              +:.........;;               ;;
       x+:;:;x             +;...........;.              +;
      .x;:;::;X.         ;+..............;;            ;x;
      ++;:::;::;x+;:;;xx;..................+;.        xxx
      ++:::::;::::...........................:+xx+++++::x;
      ++::;::;:;::......................................x;
      +x;::;::::::.......;xxx+;........;+XXXXx;........:x;
       x;:::;:;:::......+XxxxxxxXXXXXXxxxxxxxxX:.......;x.
       +x;:::::;:;:.....+xxxxxxxxxxxxxxxxxxxxxX:.......+x
       .xx;:;::::::.....;Xxxxxxxxxxxxxxxxxxxxxx:......;x.
         +x;:;;:;:;:.....:x+x+x+xx+++xxxxxx+X+.......:x;
          xx;::::;:::......:;x++++++++;++x+;........;x.
      :+:x;xxx;::::;::............................:xx
  .xx+;;:;;;+xxx;;:::;::........................;xx.
 ;x;:::;::::::;xxxxx;;;:::..................:;xxx.
.x;::::::;::;:::+xx;;;+XXxx++;;;;;;;;;;;xx+;;;.;x:
x;:;:;:::::::;:;;x;...;+x  ;; ...  ..:.    . ;:.;x;
x;:::::++xx;::::;+....;X. :;.       .:.     ..;..;X.
x;:;:::::::;x:::++...:x+  +. .  . . .:.. .   .x..:x.
:x;::;::::::x;::;+..:xx; .+........:::::..:::.+:.:x.
 ;x;::;::;:;x:;::;xxX+x; ;; .........::... . .+;+x.
  ;xx;;;;+xx;;;:;;;+x+x; .+.       ..:.    . ;+.
Hello! I'm Dawn.
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

### TC-04: Reject malformed commands without ending the session
**Aim:** Confirm that invalid task input and an unavailable task number show errors, after which `bye` still exits normally.
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

Invalid task: description cannot be blank

____________________________________________________________

____________________________________________________________

Task not found

____________________________________________________________

____________________________________________________________

${BYE}
```
