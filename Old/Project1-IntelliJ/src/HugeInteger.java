public class HugeInteger implements Comparable<HugeInteger>
{
	private int[] intList;
	public static int DIGIT_OPERATIONS;

	private HugeInteger shift(int shift)
	{
		int[] temp = new int[intList.length + shift];
		System.arraycopy(intList, 0, temp, 0, intList.length);
		intList = temp;
		return this;
	}

	private HugeInteger pad(int length)
	{
		int[] temp = new int[intList.length + length];
		System.arraycopy(intList, 0, temp, length, intList.length);
		intList = temp;
		return this;
	}

	private HugeInteger trim()
	{
		int count = 0;
		for(int i = 0; i < intList.length; i++)
		{
			if(intList[i] == 0)
				count++;
			else
				break;
		}

		if(count != 0)
		{
			int[] temp = new int[intList.length - count];
			System.arraycopy(intList, count, temp, 0, temp.length);
			intList = temp;
		}

		if(intList.length == 0)
			intList = new int[1];

		return this;
	}

	private HugeInteger high(int[] array, int length)
	{
		int[] temp = new int[length];
		System.arraycopy(array, 0, temp, 0, length);

		HugeInteger highHalf = new HugeInteger("");
		highHalf.intList = temp;
		return highHalf;
	}

	private HugeInteger low(int[] array, int offset)
	{
		int[] temp = new int[array.length - offset];
		System.arraycopy(array, offset, temp, 0, array.length - offset);

		HugeInteger lowHalf = new HugeInteger("");
		lowHalf.intList = temp;
		return lowHalf;
	}

	public HugeInteger(String s)
	{
		intList = new int[s.length()];
		for(int i = 0; i < s.length(); i++)
			intList[i] = Character.digit(s.charAt(i), 10);
	}

	public HugeInteger add(HugeInteger h)
	{
		HugeInteger result = new HugeInteger("");
		int length = Math.max(intList.length, h.intList.length);
		result.intList = new int[length + 1];
		boolean carry = false;
		int add1, add2;

		if(intList.length < length)
			this.pad(length - intList.length);
		if(h.intList.length < length)
			h.pad(length - h.intList.length);

		for(int i = length - 1; i > -1; i--)
		{
			DIGIT_OPERATIONS++;
			add1 = intList[i];
			add2 = h.intList[i];

			int sum = add1 + add2;
			if(carry)
			{
				sum++;
				carry = false;
			}
			if(sum > 9)
			{
				sum -= 10;
				carry = true;
			}
			result.intList[i + 1] = sum;
		}

		if(carry)
			result.intList[0] = 1;

		return result.trim();
	}

	// Austrian Subtraction 
	public HugeInteger subtract(HugeInteger h)
	{
		HugeInteger result = new HugeInteger("");
		int length = Math.max(intList.length, h.intList.length);
		result.intList = new int[length];
		boolean carry = false;
		int difference;

		if(intList.length < length)
			this.pad(length - intList.length);
		if(h.intList.length < length)
			h.pad(length - h.intList.length);

		for(int i = intList.length - 1; i > -1; i--)
		{
			DIGIT_OPERATIONS++;
			if(carry)
			{
				difference = intList[i] - (h.intList[i] + 1);
				carry = false;
			} else
				difference = intList[i] - h.intList[i];

			if(difference < 0)
			{
				difference = 10 + difference;
				carry = true;
			}

			result.intList[i] = difference;
		}

		return result.trim();
	}

	public HugeInteger multiply(HugeInteger h)
	{
		HugeInteger result = new HugeInteger("");
		result.intList = new int[h.intList.length * 2];
		HugeInteger partialProduct = new HugeInteger("");
		int carry = 0, shift = 0, product;

		for(int i = h.intList.length - 1; i > -1; i--)
		{
			int bottom = h.intList[i];
			if(h.intList[i] == 0)
			{
				shift++;
				continue;
			} else
			{
				partialProduct.intList = new int[intList.length + 1];
				carry = 0;

				for(int j = intList.length - 1; j > -1; j--)
				{
					DIGIT_OPERATIONS++;
					int top = intList[j];
					product = bottom * top + carry;
					partialProduct.intList[j + 1] = product % 10;
					carry = product / 10;
				}

				if(carry != 0)
					partialProduct.intList[0] = carry;

				if(shift != 0)
					partialProduct.shift(shift);

				result = result.add(partialProduct);
			}

			shift++;
		}

		return result.trim();
	}

	public HugeInteger fastMultiply(HugeInteger h)
	{
		if(h.intList.length < 50 || intList.length < 50)
			return this.multiply(h);

		HugeInteger result = new HugeInteger("");
		int length = Math.max(intList.length, h.intList.length);
		result.intList = new int[length * 2];

		if(intList.length < length)
			this.pad(length - intList.length);
		if(h.intList.length < length)
			h.pad(length - h.intList.length);

		int n = intList.length, halfLength = n / 2;

		if(n % 2 != 0)
		{
			this.pad(1);
			h.pad(1);
			halfLength = intList.length / 2;
		}

		HugeInteger a = high(intList, halfLength);
		HugeInteger b = low(intList, halfLength);
		HugeInteger c = high(h.intList, halfLength);
		HugeInteger d = low(h.intList, halfLength);
		HugeInteger ac = a.fastMultiply(c);
		HugeInteger abcd = a.add(b).fastMultiply(c.add(d));
		HugeInteger bd = b.fastMultiply(d);
		HugeInteger middle = abcd.subtract(ac).subtract(bd).shift(halfLength);

		result = ac.shift(halfLength * 2).add(middle).add(bd);

		return result.trim();
	}

	@Override
	public int compareTo(HugeInteger h)
	{
		for(int i = 0; i < intList.length; i++)
		{
			if(intList[i] < h.intList[i])
				return -1;
			if(intList[i] > h.intList[i])
				return 1;

			DIGIT_OPERATIONS++;
		}

		return 0;
	}

	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		for(Integer digit : intList)
			sb.append(digit);

		return sb.toString();
	}
}
