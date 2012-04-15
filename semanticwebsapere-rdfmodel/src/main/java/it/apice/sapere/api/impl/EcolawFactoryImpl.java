package it.apice.sapere.api.impl;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.terms.AnnotatedVarTerm;
import it.apice.sapere.api.ecolaws.terms.Formula;
import it.apice.sapere.api.ecolaws.terms.ListTerm;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.terms.PropertyTerm;
import it.apice.sapere.api.ecolaws.terms.SDescTerm;
import it.apice.sapere.api.ecolaws.terms.ValueTerm;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.SemanticDescription;
import it.apice.sapere.api.lsas.values.DoubleValue;
import it.apice.sapere.api.lsas.values.PropertyValue;

public class EcolawFactoryImpl implements it.apice.sapere.api.EcolawFactory {

	@Override
	public Ecolaw createEcolaw() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Ecolaw createEcolaw(String label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reactant createReactant(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reactant createReactant(PatternNameTerm name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product createProduct(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product createProduct(PatternNameTerm name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rate<?> createMarkovianRate(double rate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rate<?> createMarkovianRate(ValueTerm<DoubleValue> rate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rate<?> createASAPRate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Type> VarTerm<Type> createVarTerm(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Type> AnnotatedVarTerm<Type> createAnnotatedVarTerm(String name,
			Formula<Type> formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Type extends PropertyValue<?>> ValueTerm<Type> createTypedValueTerm(
			Type value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValueTerm<? extends PropertyValue<?>> createValueTerm(
			PropertyValue<?> value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Type extends PropertyValue<?>> ValueTerm<Type> createValueTerm(
			String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Type extends PropertyValue<?>> ValueTerm<Type> createValueTerm(
			String name, Formula<Type> formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Type extends PropertyValue<?>> ListTerm<Type> createListTerm(
			Type... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Type extends PropertyValue<?>> ListTerm<Type> createListTermFromTerms(
			Term<Type>... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Type extends PropertyValue<?>> ListTerm<Type> createListTerm(
			String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <Type extends PropertyValue<?>> ListTerm<Type> createListTerm(
			String name, Formula<Type> formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyTerm createPropertyTerm(PropertyName pname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyTerm createPropertyTerm(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyTerm createPropertyTerm(String name,
			Formula<PropertyName> formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDescTerm createSDescTerm(LSA lsa) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDescTerm createSDescTerm(SemanticDescription sdesc) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDescTerm createSDescTerm(PatternNameTerm pname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDescTerm createSDescTerm(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SDescTerm createSDescTerm(String name, Formula<PropertyName> formula) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PatternNameTerm createPatternNameTerm(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
