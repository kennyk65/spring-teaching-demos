package rewards.messaging;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.junit.Test;
import org.springframework.xml.xpath.Jaxp13XPathTemplate;
import org.springframework.xml.xpath.XPathOperations;

import rewards.RewardConfirmation;

public class RewardConfirmationMarshallerTest {

	RewardConfirmationMarshaller marshaller = new RewardConfirmationMarshaller();

	XPathOperations xpathTemplate = new Jaxp13XPathTemplate();

	@Test
	public void marshallDOM() throws Exception {
		RewardConfirmation rewardConfirmation = mock(RewardConfirmation.class);
		when(rewardConfirmation.getDiningTransactionId()).thenReturn("w00t");
		DOMResult result = new DOMResult();
		marshaller.marshal(rewardConfirmation, result);
		DOMSource source = new DOMSource(result.getNode());
		assertThat(xpathTemplate.evaluateAsString(
				"/reward-confirmation/@dining-transaction-id", source),
				is("w00t"));
	}

}
