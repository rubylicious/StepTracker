/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fitness;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fitness.model.Steps;
import com.fitness.repository.StepsRepository;


/**
 * DOCUMENT ME!
 *
 * @author  JDraper
 */
@Path("/steps") //
public class StepsResource {
	

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{monthYear}")
    public List<Steps> getSteps(@HeaderParam("Authorization") String token, @PathParam("monthYear") String monthYear) {
        return StepsRepository.findStepsPerMonth(monthYear, token);
    }

    /**
     * Creates a new StepsResource object.
     *
     * @param  stepsList  text  stepsList
     * @throws Exception 
     */
    @POST
    @Path("/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Steps> submitSteps(@HeaderParam("Authorization") String token, List<Steps> monthlySteps){
    	String[] info = token.split("&");
    	List<Steps> updatedSteps = new ArrayList<Steps>();
        try {
        	updatedSteps = StepsRepository.submitMonthlySteps(info[0], info[1], monthlySteps);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return updatedSteps;
    }

    // @POST
    // @Path("/submit")
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public String submitSteps(String text) {
    // System.out.println(text);
    // return text;
    // }
}
