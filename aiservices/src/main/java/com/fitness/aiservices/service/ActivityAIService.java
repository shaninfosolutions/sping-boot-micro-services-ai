package com.fitness.aiservices.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservices.model.Activity;
import com.fitness.aiservices.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity){

        String prompt=createPromptForActivity(activity);
        String aiResposne=geminiService.getAnswer(prompt);
        log.info("AI Response: {}", aiResposne);

        // processAiRespoinse(activity,aiResposne);
        return  processAiRespoinse(activity,aiResposne);
    }

    private  String createPromptForActivity(Activity activity){

    return  String.format("""
            Analyze this fitness activty and provide detailed recommendations in this format
            {
            "analysis":{
                        "overall": "Overall analysis here"
                        "pace": "Pace analysis here",
                        "heartRate": "Heart rate analysis here",
                        "caloriesBurned": "Calories analysis here",
                        },
            "improvements":[{
                        "area": "Area name",
                        "recommendation":"Detailed recommendation"
                        }
                        ],
            "suggestions":[{
                "workout": "Workout name",
                "description": "Detail workout description"
            }
            ],
            "safety": [
           "Safety point 1",
           "Safety point 2"
            ]
            }
           Analyze this activity:
           Activity Type: %s
           Duration: %d minutes
           Calories Burned: %d
           Additional Metrics: %s
           Provide detailed analysis focusing on performance, improvements, next workout recommendation in short lines
           Ensure the resposne follow the EXACT JSON format shown above
           """,activity.getType(),
            activity.getDuration(),
            activity.getCaloriesBurned(),
            activity.getAddiontalMetries());

    }

    private Recommendation processAiRespoinse(Activity activity,String aiResposne){

        try{

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode=objectMapper.readTree(aiResposne);
            JsonNode textNode=rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent=textNode.asText()
                    .replaceAll("```json\\n","")
                    .replaceAll("```\\n","")
                    .trim();

            log.info("PARSE RESPONSE FROM AI:: {}", jsonContent);

            JsonNode analysisJson=objectMapper.readTree(jsonContent);

            JsonNode analysisNode=analysisJson.path("analysis");
            StringBuilder fullAnalysis=new StringBuilder();
            addAnalysisSection(fullAnalysis,analysisNode,"overall","Overall:");
            addAnalysisSection(fullAnalysis,analysisNode,"pace","Pace:");
            addAnalysisSection(fullAnalysis,analysisNode,"heartRate","Heart Rate:");
            addAnalysisSection(fullAnalysis,analysisNode,"caloriesBurned","Calories Burned:");

            List<String> improvements=extractImprovements(analysisJson.path("improvements"));
            List<String> suggestion=extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety=extractSafetyGuideLine(analysisJson.path("safety"));

            return
            Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestion)
                    .safety(safety)
                    .createdAt(LocalDateTime.now())
                    .build();

        }catch (Exception e){
            e.printStackTrace();
            //return createDefaultRecommendatation(activity);

        }
        return null;
    }



    private List<String> extractSafetyGuideLine(JsonNode safetys) {
        List<String> safetyList=new ArrayList<>();
        if(safetys.isArray()){

            safetys.forEach(safety->{

                safetyList.add(safety.asText());
            });
        }

        return  safetyList.isEmpty()?
                Collections.singletonList("Follow general safety guideline"):safetyList;

    }

    private List<String> extractSuggestions(JsonNode suggestions) {
        List<String> suggestionList=new ArrayList<>();
        if(suggestions.isArray()){
            suggestions.forEach(suggestion->{
                String area=suggestion.path("workout").asText();
                String details=suggestion.path("description").asText();
                suggestionList.add(String.format("%s: %s",area,details));
            });
        }

        return  suggestionList.isEmpty()?
                Collections.singletonList("No specific suggestions provided"):suggestionList;
    }

    private List<String> extractImprovements(JsonNode improvements) {
        List<String> improvementsList=new ArrayList<>();
        if(improvements.isArray()){
            improvements.forEach(improvement->{
                String area=improvement.path("area").asText();
                String details=improvement.path("recommendation").asText();
                improvementsList.add(String.format("%s: %s",area,details));
            });


        }

        return  improvementsList.isEmpty()?
                Collections.singletonList("No specific improvements provided"):improvementsList;
    }



    private void addAnalysisSection(StringBuilder fullAnalysis,JsonNode analysisNode,String key,String prefix){
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(prefix)
                    .append(analysisNode.path(key).asText())
            .append("\n\n");
        }
    }

}
