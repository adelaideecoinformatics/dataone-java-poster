package au.org.ecoinformatics.eml.jaxb.eml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.eml.ListType.Listitem;

public class ListitemTest {

	/**
	 * Can we create a Listitem object with a para element using the standard JAXB method?
	 */
	@Test
	public void testPara01() {
		Listitem result = new Listitem()
			.withParaOrItemizedlistOrOrderedlist(
					new ObjectFactory().createListTypeListitemPara(
							new ParagraphType().withContent("some paragraph")));
		ParagraphType firstPara = (ParagraphType) result.getParaOrItemizedlistOrOrderedlist().get(0).getValue();
		assertThat((String) firstPara.getContent().get(0), is("some paragraph"));
	}

	/**
	 * Can we create a Listitem object with a para element using the added method?
	 */
	@Test
	public void testPara02() {
		Listitem result = new Listitem()
			.withPara(new ParagraphType().withContent("some paragraph"));
		ParagraphType firstPara = (ParagraphType) result.getParaOrItemizedlistOrOrderedlist().get(0).getValue();
		assertThat((String) firstPara.getContent().get(0), is("some paragraph"));
	}
	
	/**
	 * Can we create a Listitem object with an Itemizedlist element using the standard JAXB method?
	 */
	@Test
	public void testItemizedlist01() {
		Listitem result = new Listitem()
			.withParaOrItemizedlistOrOrderedlist(
					new ObjectFactory().createListTypeListitemItemizedlist(
							new ListType().withListitem(
									new Listitem().withPara(
											new ParagraphType().withContent("para nested under Itemizedlist")))));
		ListType firstItemizedlist = (ListType) result.getParaOrItemizedlistOrOrderedlist().get(0).getValue();
		ParagraphType firstNestedPara = (ParagraphType) firstItemizedlist.getListitem().get(0).getParaOrItemizedlistOrOrderedlist().get(0).getValue();
		assertThat((String) firstNestedPara.getContent().get(0), is("para nested under Itemizedlist"));
	}
	
	/**
	 * Can we create a Listitem object with an Itemizedlist element using the added method?
	 */
	@Test
	public void testItemizedlist02() {
		Listitem result = new Listitem()
			.withItemizedlist(
					new ListType().withListitem(
							new Listitem().withPara(
									new ParagraphType().withContent("para nested under Itemizedlist"))));
		ListType firstItemizedlist = (ListType) result.getParaOrItemizedlistOrOrderedlist().get(0).getValue();
		ParagraphType firstNestedPara = (ParagraphType) firstItemizedlist.getListitem().get(0).getParaOrItemizedlistOrOrderedlist().get(0).getValue();
		assertThat((String) firstNestedPara.getContent().get(0), is("para nested under Itemizedlist"));
	}
	
	/**
	 * Can we create a Listitem object with an Orderedlist element using the standard JAXB method?
	 */
	@Test
	public void testOrderedlist01() {
		Listitem result = new Listitem()
			.withParaOrItemizedlistOrOrderedlist(
					new ObjectFactory().createListTypeListitemItemizedlist(
							new ListType().withListitem(
									new Listitem().withPara(
											new ParagraphType().withContent("para nested under Orderedlist")))));
		ListType firstOrderedlist = (ListType) result.getParaOrItemizedlistOrOrderedlist().get(0).getValue();
		ParagraphType firstNestedPara = (ParagraphType) firstOrderedlist.getListitem().get(0).getParaOrItemizedlistOrOrderedlist().get(0).getValue();
		assertThat((String) firstNestedPara.getContent().get(0), is("para nested under Orderedlist"));
	}
	
	/**
	 * Can we create a Listitem object with an Orderedlist element using the added method?
	 */
	@Test
	public void testOrderedlist02() {
		Listitem result = new Listitem()
			.withOrderedlist(
					new ListType().withListitem(
							new Listitem().withPara(
									new ParagraphType().withContent("para nested under Orderedlist"))));
		ListType firstOrderedlist = (ListType) result.getParaOrItemizedlistOrOrderedlist().get(0).getValue();
		ParagraphType firstNestedPara = (ParagraphType) firstOrderedlist.getListitem().get(0).getParaOrItemizedlistOrOrderedlist().get(0).getValue();
		assertThat((String) firstNestedPara.getContent().get(0), is("para nested under Orderedlist"));
	}
}
