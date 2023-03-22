# DataHouseProject

 -This program takes in a JSON file in which the name is specified from the user.
 
 -It then caluclates the average scores for each attribute that exists within the collection of team members. These averages are stored in a hash map that maps the attribute name to the average score.
 
 -The program then iterates over the collection of applicant members and calculates a score in range [0,1] for each attribute. This score is calculated by: .5(applicantScore/teamAverageScore). These scores are then averaged for each individual applicant member. I used this formula because a compatibility score of .5 is average in the range [0,1]. So, if an applicant had the exact same values as the team averages for each attriute then their resulting compatibility score would be .5. A score of .5 means they are average compared to the team members. A score below .5 means below average and above means above average.
 
 -A JSON object is created for each applicant that maps their name to their compatibility score. Each of these objects is placed in the final JSON array.
 
 -A JSON object is created that maps the string "scoredApplicants" to the final JSON array that is specified above. This object is then written to a JSON file "output.json" which is our final solution.
 
 -Assumptions I made:
    -Each attribute is weighed equally
    -The set of attributes is the same for all members and each attribute has to have a score (can't be blank/null)
    -Limit on the scores for each attribute: [-∞,∞]

 
