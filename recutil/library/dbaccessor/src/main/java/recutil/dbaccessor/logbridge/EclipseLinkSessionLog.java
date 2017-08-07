package recutil.dbaccessor.logbridge;
/**https://gist.github.com/seraphy/c3598f17e3b2b946ee0a*/
import org.eclipse.persistence.internal.databaseaccess.Accessor;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JPAのEclipseLink実装のログ出力を、SLF4J経由で出力するためのセッションロガー.<br>
 * persistence.xmlで
 * {@code <property name="eclipselink.logging.logger"
 * value="jp.seraphyware.javasejpaexample.util.EclipseLinkSessionLog" />}
 * のように設定する.
 *
 * @author seraphy
 */
public class EclipseLinkSessionLog extends AbstractSessionLog {

	/**
	 * EclipseLink用のログ名.
	 */
	public static final String ECLIPSELINK_NAMESPACE = "org.eclipse.persistence";

	@Override
	public void log(SessionLogEntry logEntry) {
		int logLevel = logEntry.getLevel();
		String categoryName = logEntry.getNameSpace();
		Throwable ex = logEntry.getException();

		String logName = ECLIPSELINK_NAMESPACE + (categoryName == null ? ""
				: ("." + categoryName));
		Logger log = LoggerFactory.getLogger(logName);

		switch (logLevel) {
		case SessionLog.OFF:
			break;

		case SessionLog.SEVERE:
			if (log.isErrorEnabled()) {
				log.error(computeMessage(logEntry), ex);
			}
			break;

		case SessionLog.WARNING:
			if (log.isWarnEnabled()) {
				log.warn(computeMessage(logEntry), ex);
			}
			break;

		case SessionLog.INFO:
			if (log.isInfoEnabled()) {
				log.info(computeMessage(logEntry), ex);
			}
			break;

		case SessionLog.CONFIG:
		case SessionLog.FINE:
			if (log.isDebugEnabled()) {
				log.debug(computeMessage(logEntry), ex);
			}
			break;

		case SessionLog.FINER:
		case SessionLog.FINEST:
		case SessionLog.ALL:
		default:
			if (log.isTraceEnabled()) {
				log.trace(computeMessage(logEntry), ex);
			}
			break;
		}
	}

	/**
	 * ログメッセージの組み立て
	 * @param entry
	 * @return メッセージ
	 */
	private String computeMessage(final SessionLogEntry entry) {
		StringBuilder buf = new StringBuilder();

		// セッション識別子の表示
		if (shouldPrintSession()) {
			String sessionName = null;
			AbstractSession session = entry.getSession();
			if (session != null) {
				sessionName = getSessionString(session);
			}
			buf.append("(").append(sessionName).append(")");
		}

		// コネクション識別子の表示
		if (shouldPrintConnection()) {
			Accessor connection = entry.getConnection();
			String connectionId = null;
			if (connection != null) {
				connectionId = Integer
						.toHexString(System.identityHashCode(connection));
			}
			buf.append("(conn@").append(connectionId).append(")");
		}

		if (buf.length() > 0) {
			buf.append(" ");
		}

		// メッセージの表示
		buf.append(entry.getMessage());

		return buf.toString();
	}
}
