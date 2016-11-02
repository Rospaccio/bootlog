# Clean code - by Robert Martin

While re-reading "Clean Code" I realized that it's full of very good maxim (sometimes even one-liner) that makes a lot of sense even when taken out of context so I decided to collect those which are, in my humble opinion, the best

## About writing complicated algorithms:

> When I write functions, they come out long and complicated. They have lots of indenting and nested loops. They have long argument lists. The names are arbitrary, and there is duplicated code. But I also have a suite of unit tests that cover every one of those clumsy lines of code

## The importance of Unit Tests
> If you don't keep your tests clean, you will lose them. And without them, you lose the very thing that keeps your production code flexible flexible. Yes, you read that correctly. It is unit tests that keep our code flexible, maintainable, and reusable. The reason is simple. If you have tests, you do not fear making changes to the code! Without tests every change is a possible bug. No matter how flexible your architecture is, no matter how nicely partitioned your design, without tests you will be reluctant to make changes because of the fear that you will introduce undetected bugs.

## Writing drafts and then refining them
> Writing clean compositions [...] is a matter of successive refinement. Most freshman programmers [...] don't follow this advice particularly well. They believe that the primary goal is to get the program working. Once it's "working", they move on to the next task, leaving the "working" program in whatever state they finally got it to "work". Most seasoned programmers know that this is professional suicide

## "It's not enough..."
> It is not enough for code to work. Code that works is often badly broken. Programmers who satisfy themselves with merely working code are behaving unprofessionally. [...] Nothing has a more profound and long-term degrading effect upon a development project than bad code. [...] bad code rots and ferments, becoming an inexorable weight that drags the team down. [...] So the solution is to continuously keep your code as clean and simple as it can be. Never let the rot get started.

## About what to import in Java
> Specific imports are hard dependencies, whereas wildcard imports are not. If you specifically import a class, then that class must exist. But if you import a package with a wildcard, no particular classes need to exist. The import statement simply adds the package to the search path when hunting for names. So no true dependency is created by such imports, and they therefore serve to keep our modules less coupled
