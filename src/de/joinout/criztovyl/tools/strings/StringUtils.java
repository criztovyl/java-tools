/**
    This is a part of my tool collection.
    Copyright (C) 2014 Christoph "criztovyl" Schulz

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.joinout.criztovyl.tools.strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import de.joinout.criztovyl.tools.lists.ActionMethod;
import de.joinout.criztovyl.tools.lists.ListUtility;

/**
 * A object that helps with {@link String}s.<br>
 * To access the generated {@link String} use {@link #getString()}.
 * @author criztovyl
 *
 */
public class StringUtils {
	
	public final static int STYLE_KOMMA_AND = 0;
	public final static int STYLE_NEWLINE = 1;
	
	private String string;
	private ArrayList<String> strings;
	
	/**
	 * Sets up StringUtils with a {@link String} {@link Collection}.
	 * @param strings the {@link Collection}
	 */
	public StringUtils(Collection<String> strings){
		this.string = "";
		this.strings = new ArrayList<>(strings);
	}

	/**
	 * Sets up StringUtils for a {@link String}.
	 * @param string the {@link String}
	 */
	public StringUtils(String string){
		this.string = string;
		this.strings = new ArrayList<>();
	}
	
	public StringUtils(String ...strings){
		 this.strings = new ArrayList<>(Arrays.asList(strings));
		 string = "";
	}
	public StringUtils(){
		string = "";
		strings = new ArrayList<>();
	}

	/**
	 * Transforms an {@link String} {@link Collection} to an single string and optional break after each
	 * element.
	 * @param strings the {@link Collection}
	 * @param attachLineBreaks whether should attach line break to each element
	 * @return a {@link String}
	 * @deprecated Replaced by constructor {@link #StringUtils(Collection)} and {@link #join(int)} with 
	 * {@link #STYLE_NEWLINE}. Get string with {@link #getString()}.
	 */
	@Deprecated
	public static String fromStringCollection(Collection<String> strings, boolean attachLineBreaks){
		return attachLineBreaks ? 
				new StringUtils(strings).join(STYLE_NEWLINE) : 
					new StringUtils(strings).join("");
	}
	/**
	 * Transforms an {@link String} {@link Collection} to an single string.
	 * @param strings the {@link Collection}
	 * @return a {@link String}
	 * @deprecated Replaced by constructor {@link #StringUtils(Collection)}. Get string with 
	 * {@link #getString()}.
	 */
	@Deprecated
	public static String fromStringCollection(Collection<String> strings){
		return fromStringCollection(strings, false);
	}
	/**
	 * The string.
	 * @return a {@link String}. Maybe empty if there is a collection only.
	 */
	public String getString() {
		return string;
	}
	
	/**
	 * The strings.
	 * @return a {@link Collection}. Maybe empty if there is a string only.
	 */
	public Collection<String> getCollection(){
		return strings;
	}
	/**
	 * Joins the {@link String} {@link Collection} into a single string.
	 * @return the joined {@link String}.
	 */
	public String join(){
		return join("");
	}
	/**
	 * Joins the {@link String} {@link Collection} by a special style.<br>
	 * Styles are static fields in this class, prefixed with STYLE_<br>
	 * Current Styles:
	 * <ul>
	 * <li>{@link #STYLE_KOMMA_AND}: joins by "," and last element by "and"</li>
	 * <li>{@link #STYLE_NEWLINE}: joins elements by newlines (defined by {@link #newline()})</li>
	 * </ul>
	 * @param style the style (TODO: custom styles by objects [lambda?])
	 * @return the joined {@link String} or an empty one, if the style is unknown.
	 */
	public String join(int style){
		
		switch(style){
		case STYLE_KOMMA_AND:
			return join(",", "and");
		case STYLE_NEWLINE:
			return join(newline());
		default:
			return "";	
		}

	}
	/**
	 * Joins the {@link String} {@link Collection} by a separator.
	 * @param separator the separator
	 * @return this, for chaining.
	 */
	public String join(String separator){
		return join(separator, separator);
	}
	
	/**
	 * Joins the {@link String} {@link Collection} by a separator and the last element by a
	 * special separator.
	 * @param separator the separator
	 * @param specialSeparator the special separator
	 * @return this, for chaining
	 */
	public String join(String separator, String specialSeparator){

		String string = "";
		if(!strings.isEmpty()){

			// Get last
			String last = strings.get(strings.size()-1);

			// If only last element, set string to last.
			if(strings.size() == 1)
				string += last;
			/* If more elements, iterate over list and add each element, suffixed by the separator, to
			 * string. After iteration replace last separator with the special last separator and append
			 * last element removed above.
			 */
			else{
				for(String related : strings.subList(0, strings.size()-1))
					string += related + separator;
				string = specialSeparator.equals("") ? string += last : string.replaceAll(separator + "$", specialSeparator + last);
			}
		}
		return string;
	}
	
	/**
	 * Pops a string from the strings collection.
	 * @return a {@link String} or an empty string ("") if no strings available.
	 */
	public String pop(){
		return strings.isEmpty() ? "" : strings.remove(strings.size()-1);
	}
	
	/**
	 * Prefixes each string from the collection
	 * @param prefix the prefix
	 * @return this, for chaining.
	 */
	public StringUtils prefix(String prefix){
		return each("%2$s%1$s", prefix);
	}
	
	/**
	 * Sets the String collection.
	 * @param collection the {@link Collection}
	 * @return this, for chaining.
	 */
	private StringUtils setCollection(Collection<String> collection){
		this.strings = new ArrayList<>(collection);
		
		return this;
	}
	
	/**
	 * Shifts a string
	 * @return a String or an empty string ("") if no string is available
	 */
	public String shift(){
		return strings.size() >= 1 ? strings.remove(0) : "";
	}
	/**
	 * Splits a string by the given regular expression.<br>
	 * Refer to {@link String#split(String)}.
	 * @param regex the regular expression
	 * @return this, for chaining.
	 */
	public StringUtils split(String regex){
		this.strings = new ArrayList<>(Arrays.asList(this.string.split(regex)));
		return this;
	}
	
	/**
	 * Suffixes each string.
	 * @param suffix the suffix
	 * @return this, for chaining.
	 */
	public StringUtils suffix(String suffix){
		return each("%s%s", suffix);
	}
	/**
	 * Does a {@link String#format(String, Object...)} on each {@link Collection} element and 
	 * replaces it with the result.<br>
	 * The the element is always the first argument.
	 * @param format the format string
	 * @param args the arguments
	 * @return this, for chaining.
	 */
	private StringUtils each(final String format, Object ...args){
		
		//Create arguments ArrayList (for usage in action)
		final ArrayList<Object> args_ = new ArrayList<>(Arrays.asList(args));	
		
		// Replace list
		setCollection(new ListUtility<>(
				new ArrayList<>(getCollection())).each(new ActionMethod<String, String>() {

			public String action(String i) {
				return String.format(format, createArgumentsList(i, args_).toArray());
			}
		}));

		return this;
	}
	/**
	 * Replaces all occurrences of a regular expression in all {@link String}s.
	 * @param regex the regular expression
	 * @param replacement the replacement
	 * @return this, for chaining.
	 */
	public StringUtils replaceAll(final String regex, final String replacement){
		
		//Replace collection and do an for-each
		setCollection(new ListUtility<>(new ArrayList<>(strings)).each(
				new ActionMethod<String, String>(){

			public String action(String i) {
				
				// Replace
				return i.replaceAll(regex, replacement);
			}
			
		}));

		// Return
		return this;
	}
	/**
	 * Joins all elements from a {@link Collection} with a separator only the last element will be 
	 * separated by special separator.
	 * @param stringsC the {@link Collection}
	 * @param separator the separator
	 * @param lastSeparator the special last separator
	 * @return a {@link String}
	 * @deprecated Replaced by constructor {@link StringUtils#StringUtils(Collection)} and
	 * {@link #join(String, String)}. Get string with {@link #getString()}.
	 */	
	@Deprecated
	public static String joinCollection(Collection<String> stringsC, String separator, String lastSeparator){
		return new StringUtils(stringsC).join(separator, lastSeparator);
		
	}
	/**
	 * Joins all elements from a {@link Collection} with a separator.
	 * @param stringsC the {@link Collection}
	 * @param separator the separator
	 * @return a {@link String}
	 */
	@Deprecated
	public static String joinCollection(Collection<String> stringsC, String separator){
		return joinCollection(stringsC, separator, separator);
	}
	/**
	 * Joins all elements from a {@link Collection} with ", " only the last element will be separated by
	 * " and ".
	 * @param stringsC the {@link Collection}
	 * @return a {@link String}
	 */
	@Deprecated
	public static String joinCollection(Collection<String> stringsC){
		return joinCollection(stringsC, ", ", " and ");
	}
	/**
	 * Joins all {@link String}s with a separator. Optional last element will be separated by a special
	 * separator.
	 * @param separator the separator
	 * @param specialSeparator whether the first {@link String} from the {@link String}s should be the
	 * special separator
	 * @param strings the {@link String}s
	 * @return a {@link String}
	 */
	@Deprecated
	public static String join(String separator, boolean specialSeparator, String ...strings){
		
		//Create String list
		ArrayList<String> list = new ArrayList<>(Arrays.asList(strings));
		
		//Join
		//If special last separator should be used, "shift" it from the array (List#remove(0))
		return specialSeparator ? 
				joinCollection(list, separator, list.remove(0)) : 
					joinCollection(list, separator);
	}
	/**
	 * Pass-through to {@link #joinCollection(Collection)} by creating a {@link Collection} from
	 * {@link String}s.
	 * @param strings the {@link String}s
	 * @see StringUtils#joinCollection(Collection)
	 * @return a {@link String}
	 */
	@Deprecated
	public static String join(String ...strings){
		return joinCollection(stringCollection(strings));
	}
	/**
	 * Creates a Collection from String arguments
	 * @param strings the strings
	 * @return a {@link Collection} (a {@link ArrayList}).
	 */
	@Deprecated
	public static Collection<String> stringCollection(String ...strings){
		return new StringUtils(strings).getCollection();
	}
	/**
	 * Splits at newline, prefixes and joins by newline
	 * @param lines the string to split
	 * @param newline the string to split by
	 * @param prefix the prefix string
	 * @return a {@link String}
	 * @deprecated Replaced by {@link #StringUtils(String)}, {@link #split(String)} and 
	 * {@link #join(String)}.
	 */
	@Deprecated
	public static String prefixLines(String lines, String newline, String prefix){
		String[] linesA = lines.split(newline);
		String linesN = "";
		for(String line : linesA)
			linesN += prefix + line + newline;
		return linesN;
	}
	/**
	 * Splits at newline (defined by {@link #newline()}), prefixes and joins by newline.
	 * @param lines the lines (in one string)
	 * @param prefix the prefix
	 * @return a {@link String}.
	 * @deprecated See {@link #prefixLines(String, String, String)}.
	 */
	@Deprecated
	public static String prefixLines(String lines, String prefix){
		return prefixLines(lines, newline(), prefix);
	}
	/**
	 * 
	 * @return the OS-dependent new-line String from {@link String#format(String, Object...)} by
	 * "<code>%n</code>".  
	 */
	public static String newline(){
		return String.format("%n");
	}
	
	/**
	 * Creates the argument list for {@link #each(String, Object...)}.
	 * @param str the element
	 * @param args the arguments
	 * @return a {@link ArrayList} with the element as first element.
	 */
	private ArrayList<Object> createArgumentsList(String str, ArrayList<Object> args){
		
		// Create new List
		ArrayList<Object> args_ = new ArrayList<>();
		
		// Add element and args
		args_.add(str);
		args_.addAll(args);
		
		// Return
		return args_;
	}
}
