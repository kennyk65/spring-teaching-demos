package rewards.messaging;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.MarshallingFailureException;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import rewards.RewardConfirmation;

public class RewardConfirmationMarshaller implements Marshaller {

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(RewardConfirmation.class);
	}

	@Override
	public void marshal(Object graph, Result result) throws XmlMappingException, IOException {
		RewardConfirmation rewardConfirmation = (RewardConfirmation) graph;
		Assert.hasText(rewardConfirmation.getDiningTransactionId(),
			"'diningTransactionId' is missing from reward confirmation");
		
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (Throwable e) {
			throw new MarshallingFailureException("Can't create new Document", e);
		}
		Element elConfirmation = (Element) doc.appendChild(doc.createElement("reward-confirmation"));
		elConfirmation.setAttribute("dining-transaction-id", rewardConfirmation.getDiningTransactionId());
		
		if (result instanceof DOMResult) {
			((DOMResult) result).setNode(doc);
		} else {
			throw new IllegalArgumentException("Got instance of "
				+ result.getClass().getSimpleName()
				+ " which is not supported");
		}

	}
}
