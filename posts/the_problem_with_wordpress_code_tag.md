# A brief History of MiBECoM
## The problem with the WordPress `[code]` tag

During the last couple of years I have been using WordPress for my blog for a variety of reasons. It is free,
it's very complete in functionalities and, most important for me, it allows you to insert code fragments into your articles that are automatically laid out in a very catching way, with syntax coloring and line numbering.

But there has always been a problem with such code fragments, I would say a bug, that's become increasingly more annoying, to the point that I can no longer take it.

I usually wrap my code fragment with a `[code]` tag in the HTML editor, like this:

![HTML editor with a code tag](/images/image-01.png)

Then, when I first publish the complete article, I get something like this, which looks good:

![The resulting article with the code fragment](/images/image-02.png)

It's very common that I edit the article various times. Since I'm not native in English, very often I realize that I made a grammar mistake, or misspelled a word, after hours or days since the initial publication. When in this situation, I open an existing post for edit, I correct nothing more than the offending sentence or word, and then update the article. And this is what I get:

![broken post](/images/image-03.png)

WordPress somehow auto-escapes any character reserved to HTML. Now I have to individually fix any unwanted HTML entity occurrences, or to hunt down the original piece of code and cut-n-paste it from scratch. Multiply this for the number of time I misspell a word (thousands), and you can easily get why I'm so mad at WordPress.

## Markdown to the rescue
Parallel to my troubled relationship with WordPress, I've developed a preference for the GitHub flavored Markdown notation. I initially used it for the README file in my GitHub projects, then I started to include a README.md file in every projects I worked on, even at work. Then I started using it to write technical documentation, estimates, internal reports and whatnot.

I find that Markdown has exactly what I need when I want to write a document and the only additional software needed is a lightweight editor like Atom or Visual Studio Code, that also offer you a handy preview of the resulting HTML. It's like LaTeX's little brother.

Recently I stumbled across [Jekyll](https://jekyllrb.com/) and [GitHub Pages](https://pages.github.com/), which together are the natural evolution of the capabilities offered by Markdown and, in my opinion, a wonderful free tool to build static Web sites.

## The idea for a new prototype
With these examples in mind I developed the desire of something even more basic and simple, a system entirely based on HTML and JavaScript that offers a workflow similar to the GitHub Pages' one, but without the need to install nothing more than a Web Server.

So, in order to prototype my ideas, I started building my own blogging platform, **MiBECoM**, which is the system that is currently hosting this article.

*What's the advantage over an old school static HTML site?*, you may ask. Well, the point is that I want to write my posts in Markdown, make them as standalone as possible, save them in a folder and have them magically published. Doing so, I would be able to _version_ my contents using **Git**, which is wonderful.

MiBECoM is not much more than a frame with a Markdown interpreter, that's it. Moreover, Markdown, unlike HTML, is super easy to learn also for non computer people.

## The bright future of Markdown and Git

I think that the potential of the combination of **Markdown** plus **Git** plus a **rendering engine** is huge and it goes far beyond the community of developers and computer people.

Think about how useful it could be to teams of people that collaboratively work on technical documents (not necessarily scientific). With a small initial investment in learning, you can get a versioning system for your documents that outperforms anything available in the office suites. They could branch and merge their writings much like we developers do with our code. I'm sure that no one would want to go back to Microsoft Word's revisions or to the infamous practice of using a shared folders filled with files whose names follow eclectic naming conventions.

Just like no sane developer is missing Visual SourceSafe.
