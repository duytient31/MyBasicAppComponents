package sample.hawk.com.mybasicappcomponents.utils.logger2;

import android.util.Log;

import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class LoggerFactory {
    private static final ConcurrentMap<String, LoggerBase> LOGGER_MAP = new ConcurrentHashMap<String, LoggerBase>();

//    private static final String TAG_PREFIX = "Home";

    private static final int TAG_MAX_LENGTH = 23; // tag names cannot be longer on Android platform

    public static LoggerBase getLogger(final String name)
    {
        final String tag = getValidName(name); // fix for bug #173

        LoggerBase logger = LOGGER_MAP.get(tag);
        if (logger != null) return logger;

        logger = new LoggerBase(tag);
        LoggerBase loggerPutBefore = LOGGER_MAP.putIfAbsent(tag, logger);
        if (loggerPutBefore == null) {
            if (!tag.equals(name) && Log.isLoggable(LoggerFactory.class.getSimpleName(), Log.WARN)) {
                Log.i(LoggerFactory.class.getSimpleName(), new StringBuilder("HomeLogger name '")
                        .append(name)
                        .append("' exceeds maximum length of ")
                        .append(TAG_MAX_LENGTH)
                        .append(" characters; using '")
                        .append(tag)
                        .append("' as the Loggable property instead.")
                        .toString());
            }
            return logger;
        }
        return loggerPutBefore;
    }

    /**
     * Trim name in case it exceeds maximum length of {@value #TAG_MAX_LENGTH} characters.
     */
    public static final String getValidName(String name)
    {
        if (name != null && name.length() > TAG_MAX_LENGTH) {
            final StringTokenizer st = new StringTokenizer(name, ".");
            if (st.hasMoreTokens()) // note that empty tokens are skipped, i.e., "aa..bb" has tokens "aa", "bb"
            {
                final StringBuilder sb = new StringBuilder();
                String token;
                do
                {
                    token = st.nextToken();
                    if (token.length() == 1) // token of one character appended as is
                    {
                        sb.append(token);
                        sb.append('.');
                    }
                    else if (st.hasMoreTokens()) // truncate all but the last token
                    {
                        sb.append(token.charAt(0));
                        sb.append("*.");
                    }
                    else // last token (usually class name) appended as is
                    {
                        sb.append(token);
                    }
                } while (st.hasMoreTokens());

                name = sb.toString();
            }

            // Either we had no useful dot location at all or name still too long.
            // Take leading part and append '*' to indicate that it was truncated
            if (name.length() > TAG_MAX_LENGTH)
            {
                name = name.substring(0, TAG_MAX_LENGTH - 1) + '_';
            }
        }
        return name;
    }
}
