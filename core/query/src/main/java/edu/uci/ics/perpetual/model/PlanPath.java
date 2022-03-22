package edu.uci.ics.perpetual.model;

import java.util.ArrayList;
import java.util.List;

public class PlanPath implements Comparable<PlanPath>{
	private ObjectState object;
	private double cost;
	private double quality;
	private double benefit;
	
	public double getBenefit() {
		return benefit;
	}

	public void setBenefit(double benefit) {
		this.benefit = benefit;
	}
	private List<EnrichmentFunctionInfo> enrichmentFunctionList;
	
	public PlanPath()
	{
		//initializing list
		enrichmentFunctionList = new ArrayList<EnrichmentFunctionInfo>();
	}
	
	public ObjectState getObject() {
		return object;
	}
	public void setObject(ObjectState object) {
		this.object = object;
	}
	public double getCost() {
		return cost;
	}
	public void calculateCost() {
		cost = 0;
		// sum up all the cost of the enrichment funcitons.
		for(EnrichmentFunctionInfo f: enrichmentFunctionList)
			cost += f.getCost();
	}
	
	public void calculateBenefit() {
		cost = 0.0;
		quality = 0.0;
		// sum up all the cost of the enrichment funcitons.
		
		for(EnrichmentFunctionInfo f: enrichmentFunctionList) {
			quality +=f.getQuality();
			cost += f.getCost();
		}
			
		benefit = quality/cost;	
	}
	
	// add, get and remove for enrichmentFunctionList
	public void addEnrichmentFunction(EnrichmentFunctionInfo f)
	{
		enrichmentFunctionList.add(f);
	}
	public void addEnrichmentFunction(EnrichmentFunctionInfo f, int index)
	{
		enrichmentFunctionList.add(index, f);
	}
	public EnrichmentFunctionInfo getFunction(int index)
	{
		return enrichmentFunctionList.get(index);
	}
	public int getEnrichmentFunctionInfoSize()
	{
		return enrichmentFunctionList.size();
	}
	public EnrichmentFunctionInfo removeFunction(int index)
	{
		return enrichmentFunctionList.remove(index);
	}
	/*
	@Override
	public int compareTo(PlanPath o) {
		// TODO Auto-generated method stub
		if(this.getCost() < o.getCost())
			return -1;
		if(this.getCost() > o.getCost())
			return 1;
		return 0;
	}*/
	@Override
	public int compareTo(PlanPath o) {
		// TODO Auto-generated method stub
		if(this.getBenefit() < o.getBenefit())
			return -1;
		if(this.getBenefit() > o.getBenefit())
			return 1;
		return 0;
	}
}
