package de.bht.fpa.mail.gruppe15.model.appLogic.imap;

import de.bht.fpa.mail.gruppe15.model.data.Email;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;

/**
 * This class is responsible for converting Java Mail Message Objects (coming
 * from IMAP or POP3 accounts) to our own {@link Email} objects.
 *
 * @author Siamak Haschemi, Simone Strippgen
 */
public class IMapEmailConverter {

    /**
     * Converts a javax {@link javax.mail.Message} to an {@link Email}.
     *
     * @param message the {@link javax.mail.Message} to convert
     * @return the converted {@link Email}, or <code>null</code> if conversion
     * failed.
     */
    public static Email convertMessage(final Message message) {
        try {
            final Email mail;
            mail = new Email();
            mail.setSubject(message.getSubject());
            mail.setReceived(message.getReceivedDate());
            mail.setSent(message.getSentDate());
            mail.setRead(message.isSet(Flags.Flag.SEEN));
            mail.setImportance(convertImportance(message));
            convertContent(message, mail);
            mail.setSender(getAddress(message.getFrom()[0]));
            convertRecipients(mail, message);
            return mail;

        } catch (final MessagingException ex) {
            Logger.getLogger(IMapEmailStrategy.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private static Email.Importance convertImportance(final Message javaMailMessage) throws MessagingException {
        Email.Importance handleImportanceBasedOnXPriority = handleImportanceBasedOnXPriority(javaMailMessage);
        if (handleImportanceBasedOnXPriority != null) {
//            System.out.println("ImportanceBasedOnXPriority");
            return handleImportanceBasedOnXPriority;
        }

        Email.Importance handleImportanceBasedOnXMSMailPriority = handleImportanceBasedOnXMSMailPriority(javaMailMessage);
        if (handleImportanceBasedOnXMSMailPriority != null) {
//            System.out.println("ImportanceBasedOnXMSMailPriority");
            return handleImportanceBasedOnXMSMailPriority;
        }

        Email.Importance handleImportance = handleImportance(javaMailMessage);
        if (handleImportance != null) {
//            System.out.println("Normale Importance");
            return handleImportance;
        }
        return Email.Importance.NORMAL;
    }

    private static Email.Importance handleImportanceBasedOnXMSMailPriority(final Message javaMailMessage)
            throws MessagingException {
        final String[] header;
        header = javaMailMessage.getHeader("X-MSMail-Priority");
        if (header == null) {
            return null;
        }

        for (final String entry : header) {
            if (entry.equals("High")) {
                return Email.Importance.HIGH;
            }
            if (entry.equals("Normal")) {
                return Email.Importance.NORMAL;
            }
            if (entry.equals("Low")) {
                return Email.Importance.LOW;
            }
        }
        return null;
    }

    private static Email.Importance handleImportanceBasedOnXPriority(final Message javaMailMessage)
            throws MessagingException {

        final Pattern X_PRIORITY_VALUE_PATTERN;
        X_PRIORITY_VALUE_PATTERN = Pattern.compile("(\\d).*");
        final int X_PRIORITY_HIGH_END;
        X_PRIORITY_HIGH_END = 4;
        final int X_PRIORITY_HIGH_START;
        X_PRIORITY_HIGH_START = 2;
        final String[] header;
        header = javaMailMessage.getHeader("X-Priority");
        if (header
                == null) {
            return null;
        }
        for (final String entry : header) {
            final Matcher matcher;
            matcher = X_PRIORITY_VALUE_PATTERN.matcher(entry);
            if (!matcher.matches()) {
                continue;
            }

            final Integer flag;
            flag = Integer.valueOf(matcher.group(1));
            if (flag < X_PRIORITY_HIGH_START) {
                return Email.Importance.HIGH;
            }
            if (X_PRIORITY_HIGH_START <= flag && flag <= X_PRIORITY_HIGH_END) {
                return Email.Importance.NORMAL;
            }
            if (flag > X_PRIORITY_HIGH_END) {
                return Email.Importance.LOW;
            }
        }

        return null;
    }

    private static Email.Importance handleImportance(final Message javaMailMessage) throws MessagingException {
        final String[] header;
        header = javaMailMessage.getHeader("Importance");
        if (header == null) {
            return null;
        }

        for (final String entry : header) {
            if (entry.equals("high")) {
                return Email.Importance.HIGH;
            }
            if (entry.equals("normal")) {
                return Email.Importance.NORMAL;
            }
            if (entry.equals("low")) {
                return Email.Importance.LOW;
            }
        }
        return null;
    }

    private static String convertText(final Message message) {
        final Object cont;
        try {
            cont = message.getContent();
            if (cont instanceof String) {
                return ((String) cont);
            }
        } catch (final IOException ex) {
            Logger.getLogger(IMapEmailStrategy.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (final MessagingException ex) {
            Logger.getLogger(IMapEmailStrategy.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return "";
    }

    private static void convertContent(final Message javaMailMessage, final Email email) throws MessagingException {
        try {
            handleContent(javaMailMessage.getContent(), null, email);
        } catch (final IOException ex) {
            Logger.getLogger(IMapEmailStrategy.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void handleContent(final Object content, final BodyPart bodyPart, final Email email) throws MessagingException {
        if (content instanceof String) {
            email.setText((String) content);
        } else if (content instanceof Multipart) {
            handleMultipart((Multipart) content, email);
        } else if (content instanceof InputStream) {
            handleInputStream((InputStream) content, bodyPart, email);
        }
    }

    private static void handleInputStream(final InputStream content, final BodyPart bodyPart, final Email email) {
        String text;
        text = email.getText();
        text = text + "\n\n ------ Attachements wurden entfernt!! ------ ";
        email.setText(text);
    }

    private static void handleMultipart(final Multipart content, final Email email) throws MessagingException {
        for (int i = 0; i < content.getCount(); i++) {
            try {
                final BodyPart bodyPart;
                bodyPart = content.getBodyPart(i);
                handleContent(bodyPart.getContent(), bodyPart, email);

            } catch (final IOException ex) {
                Logger.getLogger(IMapEmailStrategy.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void convertRecipients(final Email mail, final Message message) {
        try {
//            System.out.println("Set To-Recipients");
            setRecipients(message.getRecipients(Message.RecipientType.TO), mail.getReceiverListTo());
//            System.out.println("Set CC-Recipients");
            setRecipients(message.getRecipients(Message.RecipientType.CC), mail.getReceiverListCC());
        } catch (final MessagingException ex) {
            Logger.getLogger(IMapEmailStrategy.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void setRecipients(final Address[] recipients, final ArrayList<String> receiverList) {
        if (recipients == null) {
//            System.out.println("No recipients!");
            return;
        }
        for (final Address address : recipients) {
            final String addr;
            addr = getAddress(address);
            if (addr != null) {
                receiverList.add(addr);
            }
        }
    }

    private static String getAddress(final Address address) {
        if (!(address instanceof InternetAddress)) {
            return null;
        }
        final InternetAddress internetAddress;
        internetAddress = (InternetAddress) address;
        return internetAddress.getAddress();
    }
}
