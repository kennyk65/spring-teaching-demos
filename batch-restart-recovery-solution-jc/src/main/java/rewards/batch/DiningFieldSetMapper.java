package rewards.batch;

import java.util.Date;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import rewards.Dining;

public class DiningFieldSetMapper implements FieldSetMapper<Dining> {

	//	TODO 06: Implement this method, it should create and return a Dining object based on fieldSet contents.
	//	Note the field names can be determined by examining the diningRequestsReader bean definition from the last step.
	//	Also note the Dining object has a convenient createDining() method.
	//	TODO 23: Same instructions as 06 above.
	@Override
	public Dining mapFieldSet(FieldSet fieldSet) throws BindException {
		String creditCardNumber = fieldSet.readString("creditCardNumber");
		String merchantNumber = fieldSet.readString("merchantNumber");
		String amount = fieldSet.readString("amount");
		Date date = fieldSet.readDate("date", "yyyy-MM-dd");
		return Dining.createDining(amount, creditCardNumber, merchantNumber, date);
	}

}
