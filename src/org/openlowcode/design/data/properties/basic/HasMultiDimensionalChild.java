/********************************************************************************
 * Copyright (c) 2020 [Open Lowcode SAS](https://openlowcode.com/)
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0 .
 *
 * SPDX-License-Identifier: EPL-2.0
 ********************************************************************************/

package org.openlowcode.design.data.properties.basic;

import java.io.IOException;
import java.util.ArrayList;

import org.openlowcode.design.data.DataObjectDefinition;
import org.openlowcode.design.data.Field;
import org.openlowcode.design.data.MethodAdditionalProcessing;
import org.openlowcode.design.data.Property;
import org.openlowcode.design.data.PropertyGenerics;
import org.openlowcode.design.generation.SourceGenerator;
import org.openlowcode.design.generation.StringFormatter;
import org.openlowcode.design.module.Module;



/**
 * @author <a href="https://openlowcode.com/" rel="nofollow">Open Lowcode
 *         SAS</a>
 *
 * @param <E> type of parent object
 */
public class HasMultiDimensionalChild
		extends
		Property<HasMultiDimensionalChild> {

	private DataObjectDefinition childobjectforlink;
	private MultiDimensionChild<?> originobjectproperty;

	public HasMultiDimensionalChild(String name,
			DataObjectDefinition childobjectforlink,
			MultiDimensionChild<?> originobjectproperty) {
		super(name, "HASMULTIDIMENSIONALCHILD");
		this.childobjectforlink = childobjectforlink;
		this.originobjectproperty = originobjectproperty;
		this.addPropertyGenerics(new PropertyGenerics("CHILDOBJECTFORLINK", childobjectforlink, originobjectproperty));
		
	}

	
	
	@Override
	public void controlAfterParentDefinition() {
		this.addDependentProperty(originobjectproperty.getLinkedToParent().getLinkedFromChildren());
		this.addMethodAdditionalProcessing(new  MethodAdditionalProcessing(false,this.getParent().getPropertyByName("STOREDOBJECT").getDataAccessMethod("INSERT")));
	}



	@Override
	public String[] getPropertyInitMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPropertyExtractMethod() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getPropertyDeepCopyStatement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<DataObjectDefinition> getExternalObjectDependence() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFinalSettings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getJavaType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeDependentClass(SourceGenerator sg, Module module) throws IOException {
		sg.wl("import org.openlowcode.server.data.properties.multichild.MultidimensionchildHelper;");
		sg.wl("import org.openlowcode.server.data.properties.multichild.MultichildValueHelper;");
		sg.wl("import org.openlowcode.server.data.helpers.ReportTree;");
		this.originobjectproperty.getFirstAxisValue().writeDependentClass(sg, module);
		writeImportForFieldHelper(sg,module,this.originobjectproperty.getFirstAxisValue());
		Field[] secondaxisfields = this.originobjectproperty.getSecondAxisValue();
		if (secondaxisfields!=null) for (int i=0;i<secondaxisfields.length;i++) {
			secondaxisfields[i].writeDependentClass(sg, module);
			writeImportForFieldHelper(sg,module,secondaxisfields[i]);

		}
	}

	private void writeImportForFieldHelper(SourceGenerator sg,Module module,Field field) throws IOException {
		String childclass = StringFormatter.formatForJavaClass(this.childobjectforlink.getName());
		String attributeclass = StringFormatter.formatForJavaClass(field.getName());
		sg.wl("import "+module.getPath()+".utility."+childclass+attributeclass+"FieldChildHelper;");		
	}

	@Override
	public void writeAdditionalDefinition(SourceGenerator sg) throws IOException {
String parentclass = StringFormatter.formatForJavaClass(this.getParent().getName());
String childclass = StringFormatter.formatForJavaClass(this.childobjectforlink.getName());
	
sg.wl("		hasmultidimensionalchildfor"+StringFormatter.formatForAttribute(this.getInstancename())+".setHelperGenerator(() -> {");
sg.wl("			MultidimensionchildHelper<"+childclass+","+parentclass+"> helper = new  MultidimensionchildHelper<"+childclass+","+parentclass+">((a,b)->a.setValue(ReportTree.sumIfNotNull(a.getValue(),b.getValue())));");
this.writeHelperForValue(sg,this.originobjectproperty.getFirstAxisValue(), parentclass, childclass);
Field[] secondaxisfields = this.originobjectproperty.getSecondAxisValue();
if (secondaxisfields!=null) for (int i=0;i<secondaxisfields.length;i++) {
	this.writeHelperForValue(sg,secondaxisfields[i], parentclass, childclass);

}
sg.wl("			return helper;");
sg.wl("		});		");
		
		
	
	}

	private void writeHelperForValue(SourceGenerator sg,Field field,String parentclass,String childclass) throws IOException {
		String attributeclass = StringFormatter.formatForJavaClass(field.getName());
		String attributefield = StringFormatter.formatForJavaClass(field.getName());
		sg.wl("			MultichildValueHelper<"+childclass+","+field.getJavaType()+","+parentclass+"> "+attributefield+"fieldchildhelper = new "+childclass+attributeclass+"FieldChildHelper((a,b)->a.set"+attributeclass+"(b), (a)->a.get"+attributeclass+"());");
		sg.wl("			helper.setChildHelper("+attributefield+"fieldchildhelper);");
	}
	
}