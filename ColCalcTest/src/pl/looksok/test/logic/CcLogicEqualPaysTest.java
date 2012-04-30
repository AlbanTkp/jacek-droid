package pl.looksok.test.logic;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import junit.framework.TestCase;
import pl.looksok.exception.BadPayException;
import pl.looksok.exception.DuplicatePersonNameException;
import pl.looksok.exception.PaysNotCalculatedException;
import pl.looksok.logic.CcLogic;
import pl.looksok.logic.InputData;
import pl.looksok.logic.PeoplePays;
import pl.looksok.test.utils.Constants;
import pl.looksok.test.utils.TestScenarioBuilder;

public class CcLogicEqualPaysTest extends TestCase {

	private CcLogic calc;
	private List<InputData> inputPaysList;

	public CcLogicEqualPaysTest() {
		super();
	}

	protected void setUp() throws Exception {
		calc = new CcLogic();
		inputPaysList = new ArrayList<InputData>();
		super.setUp();
	}
	
	public void testConstructor(){
		assertNotNull(calc);
	}
	
	public void testRefundOfZeroPayOnePerson(){
		inputPaysList = TestScenarioBuilder.buildTestCaseOnePerson(0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfNonZeroPayOnePerson(){
		inputPaysList = TestScenarioBuilder.buildTestCaseOnePerson(10.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
	}
	
	public void testRefundOfZeroPayFewPeople(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(0.0, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}
	
	public void testReturnOfNonZeroPayFewPeopleOnePaid(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(10.0, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_TO_RETURN, 0.0, result.get(Constants.personAName).getToReturn());
		assertEquals(Constants.INCORRECT_TO_RETURN, 5.0, result.get(Constants.personBName).getToReturn());
	}
	
	public void testRefundOfNonZeroPayFewPeopleOnePaid(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(10.0, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 5.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
	}

	public void testRefundOfNonZeroPayFewPeopleFewPaid(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(10.0, 5.0, 0.0);
		
		Hashtable<String, PeoplePays> result = calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_REFUND, 5.0, result.get(Constants.personAName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personBName).getTotalRefundForThisPerson());
		assertEquals(Constants.INCORRECT_REFUND, 0.0, result.get(Constants.personCName).getTotalRefundForThisPerson());
		
		assertEquals(Constants.INCORRECT_TO_RETURN, 0.0, result.get(Constants.personAName).getToReturn());
		assertEquals(Constants.INCORRECT_TO_RETURN, 0.0, result.get(Constants.personBName).getToReturn());
		assertEquals(Constants.INCORRECT_TO_RETURN, 5.0, result.get(Constants.personCName).getToReturn());
	}
	
	public void testThrowBadPayException(){
		try{
			inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(-10.0, 0.0);
			
			calc.calculate(inputPaysList);
			fail(Constants.SHOULD_THROW_EXCEPTION + BadPayException.class.getSimpleName());
		}catch(BadPayException e){}
	}
	
	public void testRefundOfZeroPayTwoPeopleOnePaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(0.0, 0.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void testRefundOfNonZeroPayTwoPeopleOnePaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(10.0, 0.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void testRefundOfNonZeroPayTwoPeopleTwoPaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseTwoPeople(10.0, 2.0);
		
		calc.calculate(inputPaysList);
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_TWO, 4.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleOnePaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(15.0, 0.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleOnePaidWhoToWhomV2(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(0.0, 0.0, 15.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(12.0, 3.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 5.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleThreePaidWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(11.0, 3.0, 4.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidEquallyWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(9.0, 9.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidEquallyWhoToWhomV2(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(0.0, 9.0, 9.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 3.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayThreePeopleTwoPaidNotEquallyWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(18.0, 6.0, 0.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 2.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 8.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
	}
	
	public void testRefundOfNonZeroPayFourPeopleThreePaidNotEquallyWhoToWhom(){
		inputPaysList = TestScenarioBuilder.buildTestCaseFourPeople(55.0, 36.0, 0.0, 25.0);
		
		calc.calculate(inputPaysList);
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personDName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 7.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 22.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personCName, Constants.personDName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personCName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personAName, Constants.personDName));
		
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 4.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personAName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personBName));
		assertEquals(Constants.INCORRECT_CALC_BETWEEN_THREE, 0.0, calc.howMuchPersonAGivesBackToPersonB(Constants.personDName, Constants.personCName));
	}
	
	public void testThrowExceptionIfNotCalculated(){
		try{
			inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(9.0, 9.0, 0.0);
			calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName);
			fail(Constants.SHOULD_THROW_EXCEPTION + PaysNotCalculatedException.class.getSimpleName());
		}catch (PaysNotCalculatedException e){}
	}
	
	public void testThrowExceptionIfDuplicatePersonName(){
		try{
			Constants.personBName = Constants.personAName;
			inputPaysList = TestScenarioBuilder.buildTestCaseThreePeople(9.0, 9.0, 0.0);
			calc.calculate(inputPaysList);
			calc.howMuchPersonAGivesBackToPersonB(Constants.personBName, Constants.personAName);
			fail(Constants.SHOULD_THROW_EXCEPTION + DuplicatePersonNameException.class.getSimpleName());
		}catch (DuplicatePersonNameException e){}
	}
}