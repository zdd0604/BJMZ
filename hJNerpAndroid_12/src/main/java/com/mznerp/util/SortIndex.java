package com.mznerp.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Comparator;

@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SortIndex
{
	public int value();
	
	public class SIFComparator implements Comparator<Field>
	{
		@Override
		public int compare(Field lhs, Field rhs)
		{
			boolean lnull = lhs == null;
			boolean rnull = rhs == null;
			if(lnull)
			{
				if(rnull)
				{
					return 0;
				}
				else
				{
					return -1;
				}
			}
			else if(rnull)
			{
				return 1;
			}
			else
			{
				SortIndex lsi = lhs.getAnnotation(SortIndex.class);
				SortIndex rsi = rhs.getAnnotation(SortIndex.class);
				boolean lsinull = lsi == null;
				boolean rsinull = rsi == null;
				if(lsinull)
				{
					if(rsinull)
					{
						return lhs.getName().compareTo(rhs.getName());
					}else{
						return -1;
					}
				}
				else if(rsinull)
				{
					return 1;
				}
				else
				{
					int lsiv = lsi.value();
					int rsiv = rsi.value();
					if(lsiv < rsiv)
					{
						return -1;
					}
					else if(lsiv > rsiv)
					{
						return 1;
					}
					else
					{
						return 0;
					}
				}
			}
		}
	}
}
