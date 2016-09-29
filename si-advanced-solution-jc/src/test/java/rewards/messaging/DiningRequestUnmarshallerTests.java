package rewards.messaging;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import rewards.Dining;

import common.datetime.SimpleDate;
import common.money.MonetaryAmount;

public class DiningRequestUnmarshallerTests {

	Source diningSource;
	DiningRequestUnmarshaller unmarshaller = new DiningRequestUnmarshaller();

	@Before
	public void setup() throws Exception {
		diningSource = new StreamSource(
						new ClassPathResource("dining-sample.xml", getClass()).getFile());
	}

	@Test
	public void unmarshall() throws Exception {
		Dining dining = (Dining) unmarshaller.unmarshal(diningSource);
		assertThat(dining.getAmount(), is(MonetaryAmount.valueOf("10.5")));
		assertThat(dining.getCreditCardNumber(), is("1234123412340003"));
		assertThat(dining.getMerchantNumber(), is("1234567890"));
		assertThat(dining.getDate(), is(new SimpleDate(4,21,2009)));
	}

}
