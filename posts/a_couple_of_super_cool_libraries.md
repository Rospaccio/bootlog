# A couple of super cool libraries

I know that I might be a bit late on these, but I discovered just today the beauty and power of two exceptional Java libraries that will make your life as a developer easier.

## [Lombok](https://projectlombok.org/)
prefix `@Data` to a class definition and that's it. You have getters and setters automatically generated for every private field, and `hashCode` and `equals` overridden with a reliable implementation. If that is too much for you, just try and use `@Getter` or `@Setter` before your private instance variable, and they will do exactly what you expect. Not to be overlooked, the newly created methods are recognized and listed by your favourite IDEs.

Here's a real life example of what your code may look like using Lombok:

    public @Data class ImportJobStatus
    {
        private Long id;
        private JsonImporterElement originalElement;
        private boolean running;
        private boolean success;
    }


Lombok leverages the capabilities of Java to raise its semantic power. Look at it for a moment and tell me if it doesn't seem a whole lot better than the C# Properties. Come on, it really is! I dare you. If it doesn't look cool to you, I don't know what could.

I know that, according to a lot of smart people out there, getters and setters are abused, but just give Lombok a try and see how it works for you. The results may be surprising.

## [GreenMail](http://www.icegreen.com/greenmail/)
Now how could I have lived until now without knowing about the existence of something so useful and enjoyable and powerful?

It's been one of those moments in which I think "well, ten years into this job, programming every day, trying to learn something new every day, and still I haven't stumbled across this library? Maybe I really am a jerk"

I only have been using GreenMail over an afternoon and I already feel like my life has irreversibly changed. It may be because only in the last year I have been developing some tools that read a lot of data and send an email if anything goes wrong. And only in the last three years or so I became severely obsessed with TDD. But GreenMail really is a clean and painless solution to the recurring problem of how to unit test the code that receives or sends emails.
A task that can be accomplished as easily as the following fragment, just like I've always wanted.

When you write your JUnit class, make sure to include the following rule as an instance member:

    @Rule
    public final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.ALL);

then, if you want to setup an in-memory mailbox, with an associated user, you can write something like this (don't worry about mailboxManager for now, I'll explain it later):

    @Before
     public void setup()
     {
         greenMail.setUser(RECEIVING_PEC_EMAIL_ADDRESS, RECEIVING_PEC_EMAIL_ADDRESS, TEST_PASSWORD);
         Session imapSession = greenMail.getImap().createSession();

         mailboxManager = new ImapMailboxManager(RECEIVING_PEC_EMAIL_ADDRESS,
                 TEST_PASSWORD, imapSession);
         mailboxManager.setDebug("false");
         mailboxManager.setSmtpPort(3025);
         mailboxManager.setSmtpHost("127.0.0.1");
         mailboxManager.setStartTls("false");
         mailboxManager.setProtocol("smtp");
         mailboxManager.setInboxName("INBOX");
     }

 Now you can test your mail-receiving object:

    @Test
    public void testDownloadReceivedMessages() throws MockSdiException, MessagingException
    {
        List<Message> thisShouldBeEmpty = mailboxManager.downloadUnreadMessages();
        assertNotNull(thisShouldBeEmpty);
        assertTrue(thisShouldBeEmpty.isEmpty());

        sendSMTPEmail(RECEIVING_PEC_EMAIL_ADDRESS);
        sendSMTPEmail(RECEIVING_PEC_EMAIL_ADDRESS);
        sendSMTPEmail("another@test.com");
        List<Message> messages = mailboxManager.downloadUnreadMessages();

        assertNotNull(messages.size());
        assertEquals(2, messages.size());
        messages.stream().forEach(m ->
        {
            try
            {
                assertEquals(SUBJECT, m.getSubject());
            }
            catch (MessagingException ex)
            {
                fail(ex.getMessage());
            }
        });

    //        thisShouldBeEmpty = mailboxManager.downloadUnreadMessages();
    //        assertTrue(thisShouldBeEmpty.isEmpty());
    }

To understand the previous fragment you only have to know the helper method:

    private void sendSMTPEmail(String recipient) throws MessagingException
    {
        Session smtpSession = greenMail.getSmtp().createSession();

        Message msg = new MimeMessage(smtpSession);
        msg.setFrom(new InternetAddress(SENDER));
        msg.addRecipient(Message.RecipientType.TO,
                new InternetAddress(recipient));
        msg.setSubject(SUBJECT);
        msg.setText(BODY);
        Transport.send(msg);
    }

 and, of course, that the mailboxManager variable references an object that should be able to download email messages from an IMAP server.
