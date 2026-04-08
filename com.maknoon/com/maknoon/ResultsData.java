package com.maknoon;

class ResultsData
{
	private final String inheritors;
	private final String inheritorsShare;
	private final String inheritorsCount;
	private final String inheritorsAmount;
	private final String inheritorsMowqof;

	ResultsData(final String providedInheritors, final String providedInheritorsShare, final String providedInheritorsCount, final String providedInheritorsAmount, final String providedInheritorsMowqof)
	{
		inheritors = providedInheritors;
		inheritorsShare = providedInheritorsShare;
		inheritorsCount = providedInheritorsCount;
		inheritorsAmount = providedInheritorsAmount;
		inheritorsMowqof = providedInheritorsMowqof;
	}
	
	public String getInheritors() { return inheritors; }
	public String getInheritorsShare() { return inheritorsShare; }
	public String getInheritorsCount() { return inheritorsCount; }
	public String getInheritorsAmount() { return inheritorsAmount; }
	public String getInheritorsMowqof() { return inheritorsMowqof; }

    /*
	public void setInheritors(String providedInheritors) { inheritors = providedInheritors; }
	public void setInheritorsShare(String providedInheritorsShare) { inheritorsShare = providedInheritorsShare; }
	public void setInheritorsCount(String providedInheritorsCount) { inheritorsCount = providedInheritorsCount; }
	public void setInheritorsAmount(String providedInheritorsAmount) { inheritorsAmount = providedInheritorsAmount; }
	public void setInheritorsMowqof(String providedInheritorsMowqof) { inheritorsMowqof = providedInheritorsMowqof; }
	*/
}