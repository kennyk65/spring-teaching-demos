package rewards.batch;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.item.file.FlatFileParseException;

/**
 * A log4j console appender that ignores the exceptions that the
 * {@link BatchTests} expect to be raised when running successfully.
 * <p>
 * AbstractStep outputs a long stack trace whenever it fails. Since some of
 * these tests are designed to fail, these stack-traces are ugly and misleading.
 * <p>
 * This appender only outputs the error message for
 * <tt>FlatFileParseException<tt>
 * but prints all other exceptions normally.
 */
public class TestConsoleAppender extends ConsoleAppender {

	private String NL = System.getProperty("line.separator");
	private String testName;

	/**
	 * The default setup for log4j is for none of the loggers to use their own
	 * appenders to generate output. Instead they all defer to the root-logger
	 * which has a single console appender by default. Hence all log output goes
	 * to the console.
	 * <p>
	 * Here the default appender is replaced by an instance of this class so we
	 * can manipulate the log messages and filter out the ones we wish to
	 * suppress.
	 */
	public static void setupAsDefault(Class<?> caller) {
		// Swap the root-logger's console-appender for ours.
		Logger rootLogger = Logger.getRootLogger();
		ConsoleAppender app = (ConsoleAppender) rootLogger.getAllAppenders()
				.nextElement();

		Layout layout = app.getLayout();
		String target = app.getTarget();

		rootLogger.removeAllAppenders(); // remove default
		rootLogger.addAppender(new TestConsoleAppender(caller, layout,
				target));
	}

	public TestConsoleAppender(Class<?> currentTest, Layout layout,
			String target) {
		super(layout, target);
		testName = currentTest.getSimpleName();
	}

	@Override
	public void doAppend(LoggingEvent event) {
		ThrowableInformation tInfo = event.getThrowableInformation();
		if (tInfo != null) {
			Throwable t = tInfo.getThrowable();

			if ((t instanceof FlatFileParseException || t instanceof JobExecutionException)
					&& event.getLevel().equals(Level.ERROR)) {
				// Suppress unwanted stack trace - we are expecting this
				// exception. Just report as a warning - and any cause(s).
				Logger logger = Logger.getLogger(event.getLoggerName());
				StringBuilder message = new StringBuilder();

				message.append("This error expected when ");
				message.append(testName);
				message.append(" runs correctly:");
				message.append(NL);

				if (BatchTests.SUPRRESS_EXPECTED_ERRORS) { // Just add details
															// to
					// warning
					// Indent subsequent lines of error message
					String margin = "            ";

					message.append(margin);
					message.append(t.getLocalizedMessage());

					while ((t = t.getCause()) != null) {
						message.append(NL);
						message.append(margin);
						message.append("caused by - ");
						message.append(t.getLocalizedMessage());
					}
				}

				super.doAppend(new LoggingEvent(event.fqnOfCategoryClass,
						logger, Level.WARN, message.toString(),
						BatchTests.SUPRRESS_EXPECTED_ERRORS ? null : t));
				return;
			}
		}

		// Output all other log messages normally
		super.doAppend(event);
	}

}
