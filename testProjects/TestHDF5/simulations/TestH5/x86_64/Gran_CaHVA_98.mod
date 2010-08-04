COMMENT

   **************************************************
   File generated by: neuroConstruct v1.4.1 
   **************************************************

   This file holds the implementation in NEURON of the Cell Mechanism:
   Gran_CaHVA_98 (Type: Channel mechanism, Model: Template based ChannelML file)

   with parameters: 
   /channelml/@units = SI Units 
   /channelml/notes = A channel from Maex, R and De Schutter, E. Synchronization of Golgi and Granule Cell Firing in a      Detailed Network Model of the Cerebellar Granule ... 
   /channelml/channel_type/@name = Gran_CaHVA_98 
   /channelml/channel_type/status/@value = stable 
   /channelml/channel_type/status/comment = Verified equivalence of NEURON and GENESIS mapping to orig GENESIS impl from www.tnb.ua.ac.be. 
   /channelml/channel_type/status/comment = Verified equivalence of NEURON and GENESIS mapping to orig GENESIS impl from www.tnb.ua.ac.be. 
   /channelml/channel_type/status/contributor/name = Padraig Gleeson 
   /channelml/channel_type/notes = A High Voltage Activated Ca2+ channel 
   /channelml/channel_type/authorList/modelAuthor[1]/name = Maex, R. 
   /channelml/channel_type/authorList/modelAuthor[2]/name = De Schutter, E. 
   /channelml/channel_type/authorList/modelTranslator/name = Padraig Gleeson 
   /channelml/channel_type/authorList/modelTranslator/institution = UCL 
   /channelml/channel_type/authorList/modelTranslator/email = p.gleeson - at - ucl.ac.uk 
   /channelml/channel_type/publication/fullTitle = Maex, R and De Schutter, E.             Synchronization of Golgi and Granule Cell Firing in a Detailed Network Model of the             cerebellar Gra ... 
   /channelml/channel_type/publication/pubmedRef = http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=Retrieve&amp;db=PubMed&amp;list_uids=9819260&amp;dopt=Abstract 
   /channelml/channel_type/neuronDBref/modelName = Calcium channels 
   /channelml/channel_type/neuronDBref/uri = http://senselab.med.yale.edu/senselab/NeuronDB/channelGene2.htm#table1 
   /channelml/channel_type/current_voltage_relation/@cond_law = ohmic 
   /channelml/channel_type/current_voltage_relation/@ion = ca 
   /channelml/channel_type/current_voltage_relation/@default_gmax = 9.084216 
   /channelml/channel_type/current_voltage_relation/@default_erev = 0.080 
   /channelml/channel_type/current_voltage_relation/@charge = 2 
   /channelml/channel_type/current_voltage_relation/@fixed_erev = yes 
   /channelml/channel_type/current_voltage_relation/q10_settings/@q10_factor = 3 
   /channelml/channel_type/current_voltage_relation/q10_settings/@experimental_temp = 17.350264793 
   /channelml/channel_type/current_voltage_relation/offset/@value = 0.010 
   /channelml/channel_type/current_voltage_relation/gate[1]/@name = m 
   /channelml/channel_type/current_voltage_relation/gate[1]/@instances = 2 
   /channelml/channel_type/current_voltage_relation/gate[1]/closed_state/@id = m0 
   /channelml/channel_type/current_voltage_relation/gate[1]/open_state/@id = m 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[1]/@name = alpha 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[1]/@from = m0 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[1]/@to = m 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[1]/@expr_form = sigmoid 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[1]/@rate = 1600 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[1]/@scale = -0.01388888889 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[1]/@midpoint = 0.005 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[2]/@name = beta 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[2]/@from = m 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[2]/@to = m0 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[2]/@expr_form = exp_linear 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[2]/@rate = 100 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[2]/@scale = -0.005 
   /channelml/channel_type/current_voltage_relation/gate[1]/transition[2]/@midpoint = -0.0089 
   /channelml/channel_type/current_voltage_relation/gate[2]/@name = h 
   /channelml/channel_type/current_voltage_relation/gate[2]/@instances = 1 
   /channelml/channel_type/current_voltage_relation/gate[2]/closed_state/@id = h0 
   /channelml/channel_type/current_voltage_relation/gate[2]/open_state/@id = h 
   /channelml/channel_type/current_voltage_relation/gate[2]/transition[1]/@name = alpha 
   /channelml/channel_type/current_voltage_relation/gate[2]/transition[1]/@from = h0 
   /channelml/channel_type/current_voltage_relation/gate[2]/transition[1]/@to = h 
   /channelml/channel_type/current_voltage_relation/gate[2]/transition[1]/@expr_form = generic 
   /channelml/channel_type/current_voltage_relation/gate[2]/transition[1]/@expr = v  &lt; -0.060 ? 5.0 : 5 * (exp (-50 * (v - (-0.060)))) 
   /channelml/channel_type/current_voltage_relation/gate[2]/transition[2]/@name = beta 
   /channelml/channel_type/current_voltage_relation/gate[2]/transition[2]/@from = h 
   /channelml/channel_type/current_voltage_relation/gate[2]/transition[2]/@to = h0 
   /channelml/channel_type/current_voltage_relation/gate[2]/transition[2]/@expr_form = generic 
   /channelml/channel_type/current_voltage_relation/gate[2]/transition[2]/@expr = v  &lt; -0.060 ? 0 : 5 - (5 * (exp (-50 * (v - (-0.060))))) 

// File from which this was generated: /home/padraig/neuroConstruct/testProjects/TestHDF5/cellMechanisms/Gran_CaHVA_98/CaHVA_Chan.xml

// XSL file with mapping to simulator: /home/padraig/neuroConstruct/testProjects/TestHDF5/cellMechanisms/Gran_CaHVA_98/ChannelML_v1.8.1_NEURONmod.xsl

ENDCOMMENT


?  This is a NEURON mod file generated from a ChannelML file

?  Unit system of original ChannelML file: SI Units

COMMENT
    A channel from Maex, R and De Schutter, E. Synchronization of Golgi and Granule Cell Firing in a 
    Detailed Network Model of the Cerebellar Granule Cell Layer
ENDCOMMENT

TITLE Channel: Gran_CaHVA_98

COMMENT
    A High Voltage Activated Ca2+ channel
ENDCOMMENT


UNITS {
    (mA) = (milliamp)
    (mV) = (millivolt)
    (S) = (siemens)
    (um) = (micrometer)
    (molar) = (1/liter)
    (mM) = (millimolar)
    (l) = (liter)
}


    
NEURON {
      

    SUFFIX Gran_CaHVA_98
    USEION ca WRITE ica VALENCE 2 ?  outgoing current is written
           
        
    RANGE gmax, gion
    
    RANGE minf, mtau
    
    RANGE hinf, htau
    
}

PARAMETER { 
      

    gmax = 0.0009084216 (S/cm2)  ? default value, should be overwritten when conductance placed on cell
    
}



ASSIGNED {
      

    v (mV)
    
    celsius (degC)
          

    ? Reversal potential of ca
    eca (mV)
    ? The outward flow of ion: ca calculated by rate equations...
    ica (mA/cm2)
    
    
    gion (S/cm2)
    minf
    mtau (ms)
    hinf
    htau (ms)
    
}

BREAKPOINT { 
                        
    SOLVE states METHOD cnexp
         

    gion = gmax*((m)^2)*((h)^1)      

    ica = gion*(v - eca)
            

}



INITIAL {
    
    eca = 80
        
    rates(v)
    m = minf
        h = hinf
        
    
}
    
STATE {
    m
    h
    
}

DERIVATIVE states {
    rates(v)
    m' = (minf - m)/mtau
    h' = (hinf - h)/htau
    
}

PROCEDURE rates(v(mV)) {  
    
    ? Note: not all of these may be used, depending on the form of rate equations
    LOCAL  alpha, beta, tau, inf, gamma, zeta, temp_adj_m, A_alpha_m, B_alpha_m, Vhalf_alpha_m, A_beta_m, B_beta_m, Vhalf_beta_m, temp_adj_h, A_alpha_h, B_alpha_h, Vhalf_alpha_h, A_beta_h, B_beta_h, Vhalf_beta_h
        
    TABLE minf, mtau,hinf, htau
 DEPEND celsius
 FROM -100 TO 100 WITH 400
    
    
    UNITSOFF
    
    ? There is a Q10 factor which will alter the tau of the gates 
                 

    temp_adj_m = 3^((celsius - 17.350264793)/10)     

    temp_adj_h = 3^((celsius - 17.350264793)/10)
    
    ? There is a voltage offset of 0.010. This will shift the dependency of the rate equations 
    v = v - (10)
    
            
                
           

        
    ?      ***  Adding rate equations for gate: m  ***
        
    ? Found a parameterised form of rate equation for alpha, using expression: A / (1 + exp((v-Vhalf)/B))
    A_alpha_m = 1600
    B_alpha_m = -0.01388888889
    Vhalf_alpha_m = 0.005   
    
    ? Unit system in ChannelML file is SI units, therefore need to convert these to NEURON quanities...
    
    A_alpha_m = A_alpha_m * 0.001   ? 1/ms
    B_alpha_m = B_alpha_m * 1000   ? mV
    Vhalf_alpha_m = Vhalf_alpha_m * 1000   ? mV
          
                     
    alpha = A_alpha_m / (exp((v - Vhalf_alpha_m) / B_alpha_m) + 1)
    
    
    ? Found a parameterised form of rate equation for beta, using expression: A*((v-Vhalf)/B) / (1 - exp(-((v-Vhalf)/B)))
    A_beta_m = 100
    B_beta_m = -0.005
    Vhalf_beta_m = -0.0089   
    
    ? Unit system in ChannelML file is SI units, therefore need to convert these to NEURON quanities...
    
    A_beta_m = A_beta_m * 0.001   ? 1/ms
    B_beta_m = B_beta_m * 1000   ? mV
    Vhalf_beta_m = Vhalf_beta_m * 1000   ? mV
          
                     
    beta = A_beta_m * vtrap((v - Vhalf_beta_m), B_beta_m)
    
    mtau = 1/(temp_adj_m*(alpha + beta))
    minf = alpha/(alpha + beta)
          
       
    
    ?     *** Finished rate equations for gate: m ***
    

    
            
                
           

        
    ?      ***  Adding rate equations for gate: h  ***
         
    ? Found a generic form of the rate equation for alpha, using expression: v  < -0.060 ? 5.0 : 5 * (exp (-50 * (v - (-0.060))))
    
    ? Note: Equation (and all ChannelML file values) in SI Units so need to convert v first...
    
    v = v * 0.001   ? temporarily set v to units of equation...
            
    
    
    if (v  < -0.060 ) {
        alpha =  5.0 
    } else {
        alpha =  5 * (exp (-50 * (v - (-0.060))))
    }
    ? Set correct units of alpha for NEURON
    alpha = alpha * 0.001 
    
    v = v * 1000   ? reset v
        
     
    ? Found a generic form of the rate equation for beta, using expression: v  < -0.060 ? 0 : 5 - (5 * (exp (-50 * (v - (-0.060)))))
    
    ? Note: Equation (and all ChannelML file values) in SI Units so need to convert v first...
    
    v = v * 0.001   ? temporarily set v to units of equation...
            
    
    
    if (v  < -0.060 ) {
        beta =  0 
    } else {
        beta =  5 - (5 * (exp (-50 * (v - (-0.060)))))
    }
    ? Set correct units of beta for NEURON
    beta = beta * 0.001 
    
    v = v * 1000   ? reset v
        
    htau = 1/(temp_adj_h*(alpha + beta))
    hinf = alpha/(alpha + beta)
          
       
    
    ?     *** Finished rate equations for gate: h ***
    

         

}


? Function to assist with parameterised expressions of type linoid/exp_linear

FUNCTION vtrap(VminV0, B) {
    if (fabs(VminV0/B) < 1e-6) {
    vtrap = (1 + VminV0/B/2)
}else{
    vtrap = (VminV0 / B) /(1 - exp((-1 *VminV0)/B))
    }
}

UNITSON


