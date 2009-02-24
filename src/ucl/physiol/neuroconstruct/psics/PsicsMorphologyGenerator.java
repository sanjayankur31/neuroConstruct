/**
 *  neuroConstruct
 *  Software for developing large scale 3D networks of biologically realistic neurons
 * 
 *  Copyright (c) 2009 Padraig Gleeson
 *  UCL Department of Neuroscience, Physiology and Pharmacology
 *
 *  Development of this software was made possible with funding from the
 *  Medical Research Council and the Wellcome Trust
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package ucl.physiol.neuroconstruct.psics;

import java.io.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Vector;
import javax.vecmath.Point3f;
import ucl.physiol.neuroconstruct.cell.*;
import ucl.physiol.neuroconstruct.cell.utils.*;
import ucl.physiol.neuroconstruct.project.*;
import ucl.physiol.neuroconstruct.utils.*;
import ucl.physiol.neuroconstruct.utils.units.UnitConverter;
import ucl.physiol.neuroconstruct.utils.xml.*;


/**
 * A single PSICS morphology file from a Cell object
 *
 * @author Padraig Gleeson
 *  
 */

public class PsicsMorphologyGenerator
{
    ClassLogger logger = new ClassLogger("PsicsMorphologyGenerator");

    Cell cell = null;

    File cellFile = null;
    File membFile = null;
    
    Project project = null;
    
    

    private PsicsMorphologyGenerator()
    {
    }


    public PsicsMorphologyGenerator(Cell cell,
                                      Project project,
                                      File dirForFile)
    {
        logger.logComment("PsicsMorphologyGenerator created for: " + cell.toString());
        this.cell = cell;

        StringBuffer spaceLessName = new StringBuffer();

        this.project = project;

        for (int i = 0; i < cell.getInstanceName().length(); i++)
        {
            char c = cell.getInstanceName().charAt(i);
            if (c != ' ') spaceLessName.append(c);
        }

        cellFile = new File(dirForFile, spaceLessName + ".xml");
        membFile = new File(dirForFile, "membrane_"+spaceLessName + ".xml");


    }




    public void generateFiles() throws PsicsException
    {
        logger.logComment("Starting generation of template files: " + cellFile+", "+ membFile);

        FileWriter fwCell = null;
        FileWriter fwMemb = null;
        try
        {
            fwCell = new FileWriter(cellFile);

            //fwCell.write(PsicsFileManager.getFileHeader());
            
            fwCell.write(this.getMainMorphology());

            fwCell.flush();
            fwCell.close();
        }
        catch (Exception ex)
        {
            logger.logError("Error writing to file: " + cellFile, ex);
            try
            {
                fwCell.flush();
                fwCell.close();
            }
            catch (IOException ex1)
            {
            }
            throw new PsicsException("Error writing to file: " + cellFile, ex);

        }
        try
        {
            fwMemb = new FileWriter(membFile);

            //fwMemb.write(PsicsFileManager.getFileHeader());
            
            fwMemb.write(this.getMembraneProps());

            fwMemb.flush();
            fwMemb.close();
        }
        catch (Exception ex)
        {
            logger.logError("Error writing to file: " + membFile, ex);
            try
            {
                fwMemb.flush();
                fwMemb.close();
            }
            catch (IOException ex1)
            {
            }
            throw new PsicsException("Error writing to file: " + membFile, ex);

        }

    }
    

    private String getMainMorphology()
    {
        logger.logComment("calling getMainMorphology");
        StringBuffer response = new StringBuffer();
        
        SimpleXMLElement cellMorph = new SimpleXMLElement("CellMorphology");
        
        
        SimpleXMLAttribute id = new SimpleXMLAttribute("id", cell.getInstanceName());
        cellMorph.addAttribute(id);

        ArrayList<Section> secs = cell.getAllSections();
        Hashtable<String, String> labelsForSections = new Hashtable<String, String>();
        for(Section sec: secs)
        {
            StringBuffer label = new StringBuffer();
            for(String grp: sec.getGroups())
            {
                label.append("~"+grp+"~");
            }
            labelsForSections.put(sec.getSectionName(), label.toString());
        }

        Vector<Segment> segs = cell.getAllSegments();
        //Segment lastSeg = null;
        
        for(Segment seg: segs)
        {
            SimpleXMLElement distPoint = new SimpleXMLElement("Point");
            Point3f prox = seg.getStartPointPosition();
            Point3f dist = seg.getEndPointPosition();
            Segment parent = seg.getParentSegment();
            
            // <Point parent="p0" id="p1" x="1000" y="0" z = "0" r="0.5"/>
            
            String parentId = null;
            
            //if (parent != null)
            //{    
            Point3f connectionPointParent = null;
            
            if (parent != null)
                connectionPointParent = parent.getPointAlong(seg.getFractionAlongParent());
            else
                connectionPointParent = seg.getStartPointPosition();

            boolean minor = false;

            if (!connectionPointParent.equals(prox))
            {

                GuiUtils.showErrorMessage(logger,
                                      "Error. Cell: "+cell+" is discontinuous, such cells are not supported in the mapping to PSICS yet!\n" +
                                      "Segment: "+ seg+"\nis not connected to the point "+seg.getFractionAlongParent()+" along parent segment: "+ parent,
                                      null, null);

                return "";
            }
            if (seg.isSpherical())
            {

                GuiUtils.showWarningMessage(logger,
                                      "Warning. Cell: "+cell+" has a spherical segment. The behaviour of cells with such segments when mapped to PSICS\n" +
                                      "will not fully match NEURON & GENESIS implementations at present, due to the default method of connection\n" +
                                      "in PSICS to spherical points.",
                                      null);
            }
            float connectionPointRadius = -1;

            if (parent==null)
            {
                connectionPointRadius = seg.getSegmentStartRadius();
            }
            else if (seg.getFractionAlongParent()==0)
            {
                connectionPointRadius = parent.getSegmentStartRadius();
            }
            else if(seg.getFractionAlongParent()==1)
            {
                connectionPointRadius = parent.getRadius();
            }
            else if(parent.isSpherical())
            {
                connectionPointRadius = parent.getRadius();
            }
            else
            {
                GuiUtils.showErrorMessage(logger,
                                      "Error. Cell has a segment connected between the end points of a parent segment, such cells are not supported in the mapping to PSICS yet!\n" +
                                      "Segment: "+ seg+"\nis not connected to the 0 or 1 point along (cylindrical) parent segment: "+ parent,
                                      null, null);

                return "";
            }



            if (connectionPointRadius==seg.getSegmentStartRadius() && parent!=null)
            {
                // use existing parent point...
                parentId = parent.getSegmentName();
            }
            else
            {
                if (seg.isSpherical())
                {
                    // just use distal point...
                }
                else
                {
                    SimpleXMLElement proxPoint = new SimpleXMLElement("Point");

                    String newPointId = seg.getSegmentName();
                        
                    if(parent!=null)
                    {
                        proxPoint.addAttribute(new SimpleXMLAttribute("parent", parent.getSegmentName()));
                        newPointId = seg.getSegmentName()+"_minor";
                    }
                    else
                    {
                        newPointId = newPointId+"_root";
                    }

                    SimpleXMLAttribute segIdProx = new SimpleXMLAttribute("id", newPointId);

                    proxPoint.addAttribute(segIdProx);

                    parentId = newPointId;

                    SimpleXMLAttribute xDistProx = new SimpleXMLAttribute("x", connectionPointParent.x+"");
                    SimpleXMLAttribute yDistProx = new SimpleXMLAttribute("y", connectionPointParent.y+"");
                    SimpleXMLAttribute zDistProx = new SimpleXMLAttribute("z", connectionPointParent.z+"");
                    SimpleXMLAttribute rDistProx = new SimpleXMLAttribute("r", seg.getSegmentStartRadius()+"");
                    SimpleXMLAttribute label = new SimpleXMLAttribute("label", labelsForSections.get(seg.getSection().getSectionName()));

                    proxPoint.addAttribute(xDistProx);
                    proxPoint.addAttribute(yDistProx);
                    proxPoint.addAttribute(zDistProx);
                    proxPoint.addAttribute(rDistProx);
                    if(parent!=null)
                    {
                        SimpleXMLAttribute minorProx = new SimpleXMLAttribute("minor", "true");
                        proxPoint.addAttribute(minorProx);
                    }
                    proxPoint.addAttribute(label);


                    cellMorph.addChildElement(proxPoint);
                    cellMorph.addContent("\n    ");
                }


            }
            
            //}
            
            if (parentId!=null)
            {
                distPoint.addAttribute(new SimpleXMLAttribute("parent", parentId));
            }
                
            
            SimpleXMLAttribute segIdDist = new SimpleXMLAttribute("id", seg.getSegmentName());
            SimpleXMLAttribute xDist = new SimpleXMLAttribute("x", dist.x+"");
            SimpleXMLAttribute yDist = new SimpleXMLAttribute("y", dist.y+"");
            SimpleXMLAttribute zDist = new SimpleXMLAttribute("z", dist.z+"");
            SimpleXMLAttribute rDist = new SimpleXMLAttribute("r", seg.getRadius()+"");
            SimpleXMLAttribute label = new SimpleXMLAttribute("label", labelsForSections.get(seg.getSection().getSectionName()));
            
            distPoint.addAttribute(segIdDist);
            distPoint.addAttribute(xDist);
            distPoint.addAttribute(yDist);
            distPoint.addAttribute(zDist);
            distPoint.addAttribute(rDist);
            distPoint.addAttribute(label);
            
            
            cellMorph.addChildElement(distPoint);
            cellMorph.addContent("\n    ");
            
        
        }
            
        cellMorph.addContent("\n");
        
        response.append(cellMorph.getXMLString("", false));
        

        return response.toString();
    }



    private String getMembraneProps()
    {
        //int prefUnits = UnitConverter.NEUROCONSTRUCT_UNITS;
        
        logger.logComment("calling getMainMorphology");
        StringBuffer response = new StringBuffer();
        
        response.append("\n<!-- NOTE: Uniform cell properties based on what's set for group: all!!!!-->\n\n");
        
        SimpleXMLElement memb = new SimpleXMLElement("CellProperties");
        
        SimpleXMLAttribute id = new SimpleXMLAttribute("id", "membrane_"+cell.getInstanceName());
        memb.addAttribute(id);



        String unitSpAxRes = "ohm_m";
        String unitSpCap = "F_per_m2";
        String unitDens = "per_um2";

        
        float spAxRResNc = cell.getSpecAxResForGroup("all");

        if (Float.isNaN(spAxRResNc))
        {
            // just use the first...
            spAxRResNc = cell.getSpecAxResVsGroups().keys().nextElement();
        }
    
        double spAxResFactor = UnitConverter.getSpecificAxialResistance(1, UnitConverter.NEUROCONSTRUCT_UNITS, UnitConverter.GENESIS_SI_UNITS);

        double spAxRRes = spAxResFactor * spAxRResNc;
            
        
        SimpleXMLAttribute cytRes = new SimpleXMLAttribute("cytoplasmResistivity", spAxRRes+unitSpAxRes);
        memb.addAttribute(cytRes);

        Enumeration<Float> spResVals = cell.getSpecAxResVsGroups().keys();
        while(spResVals.hasMoreElements())
        {
            float nextSpRes = spResVals.nextElement();

            for(String group: cell.getSpecAxResVsGroups().get(nextSpRes))
            {
                if (!group.equals(Section.ALL))
                {
                    SimpleXMLElement pp = new SimpleXMLElement("PassiveProperties");
                    pp.addAttribute("region", group);
                    pp.addAttribute("cytoplasmResistivity", (nextSpRes*spAxResFactor)+unitSpAxRes);

                    memb.addChildElement(pp);
                    memb.addContent("\n");
                }
            }
        }
        
        
        float spCapNc = cell.getSpecCapForGroup("all");

        if (Float.isNaN(spCapNc))
        {
            // just use the first...
            spCapNc = cell.getSpecCapVsGroups().keys().nextElement();
        }
        double spCapFactor = UnitConverter.getSpecificCapacitance(1, UnitConverter.NEUROCONSTRUCT_UNITS, UnitConverter.GENESIS_SI_UNITS);
        double spCap = spCapFactor * spCapNc;
        
        
        SimpleXMLAttribute spCapAttr = new SimpleXMLAttribute("membraneCapacitance", spCap+unitSpCap);
        memb.addAttribute(spCapAttr);


        Enumeration<Float> spCapVals = cell.getSpecCapVsGroups().keys();

        while(spCapVals.hasMoreElements())
        {
            float nextCapRes = spCapVals.nextElement();

            for(String group: cell.getSpecCapVsGroups().get(nextCapRes))
            {
                if (!group.equals(Section.ALL))
                {
                    SimpleXMLElement pp = new SimpleXMLElement("PassiveProperties");
                    pp.addAttribute("region", group);
                    pp.addAttribute("membraneCapacitance", (nextCapRes*spCapFactor)+unitSpCap);

                    memb.addChildElement(pp);
                    memb.addContent("\n");
                }
            }
        }
        
        memb.addContent("\n");


        
        Enumeration<ChannelMechanism> chans = cell.getChanMechsVsGroups().keys();

        int colourNum = 2; // Not black first...

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(12);

        while(chans.hasMoreElements())
        {
            ChannelMechanism cm = chans.nextElement();
            Vector<String> groups = cell.getChanMechsVsGroups().get(cm);
            for(String group:groups)
            {
               
                // <ChannelPopulation channel="k1" density="20per_um2" distribution="start"/>
                SimpleXMLElement cp = new SimpleXMLElement("ChannelPopulation");
                cp.addAttribute(new SimpleXMLAttribute("channel", cm.getName()));

                float condDensity = cm.getDensity(); // mS/um2
                float defaultSingChanCond_pS = project.psicsSettings.getSingleChannelCond()*1e9f; // pS
                float defaultSingChanCond_mS = defaultSingChanCond_pS/1e9f;
                float numPerum2 = condDensity/defaultSingChanCond_mS;

                cp.addAttribute(new SimpleXMLAttribute("density", df.format(numPerum2)+unitDens));
                cp.addAttribute(new SimpleXMLAttribute("color", "0x"+ColourUtils.getSequentialColourHex(colourNum)));
                colourNum++;

                memb.addChildElement(cp);
                if(!group.equals(Section.ALL))
                {
                    SimpleXMLElement rm = new SimpleXMLElement("RegionMask");
                    rm.addAttribute(new SimpleXMLAttribute("action", "include"));
                    rm.addAttribute(new SimpleXMLAttribute("where", "region = *"+group+"*"));
                    cp.addChildElement(rm);
                    cp.addContent("\n");
                }
                memb.addContent("\n\n");

              
            }
        }
        
        
        
        response.append(memb.getXMLString("", false));

        return response.toString();
    }

    public File getCellFile()
    {
        return cellFile;
    }

    public File getMembFile()
    {
        return membFile;
    }

    




    public static void main(String[] args)
    {
        try
        {
            Project testProj = Project.loadProject(new File("models/BioMorph/BioMorph.neuro.xml"),
                                                   new ProjectEventListener()
            {
                public void tableDataModelUpdated(String tableModelName)
                {};

                public void tabUpdated(String tabName)
                {};
                public void cellMechanismUpdated()
                {
                };

            });

            //SimpleCell cell = new SimpleCell("DummyCell");
            //ComplexCell cell = new ComplexCell("DummyCell");

            Cell cell = testProj.cellManager.getCell("LongCellDelayLine");

            //File f = new File("/home/padraig/temp/tempNC/NEURON/PatTest/basics/");
            File f = new File("../temp");

            PsicsMorphologyGenerator cellTemplateGenerator1 = new PsicsMorphologyGenerator(cell,testProj,
                f);

            cellTemplateGenerator1.generateFiles();

            System.out.println("Generated: " + cellTemplateGenerator1.getCellFile().getAbsolutePath()+" and "+ cellTemplateGenerator1.getMembraneProps());

            System.out.println(CellTopologyHelper.printDetails(cell, null));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
