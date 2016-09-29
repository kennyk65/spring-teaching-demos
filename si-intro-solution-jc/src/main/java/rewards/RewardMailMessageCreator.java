package rewards;

import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;

import rewards.internal.account.Account;
import rewards.internal.account.AccountRepository;

public class RewardMailMessageCreator {
	private AccountRepository accountRepository;
	
	public RewardMailMessageCreator(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public MailMessage createMail(RewardConfirmation confirmation) {
		String accountNumber = confirmation.getAccountContribution().getAccountNumber();
		Account account = accountRepository.findByAccountNumber(accountNumber);
		
		MailMessage message = new SimpleMailMessage();
		message.setFrom("rewardnetwork@example.com");
		message.setTo(account.getEmail());
		message.setSubject("New Reward");
		message.setText("You've been rewarded with " + confirmation.getAccountContribution().getAmount());
		
		return message;
	}
}
