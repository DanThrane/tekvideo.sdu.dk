(#93 + #92 + #98) + Rework of dashboard queries
#97 Guest prompt
#94 Report error

# Dashboard rework

Current issues:

  - Lack of explanation of queries
  - Lack of explanation when no data is available
  - No general interface for queries, makes it hard to extend
  - Missing features
    + Details for students
  - Lacks certain types of information (e.g. visits)
  - "Home" page is not very nice
    + Ideally we would want to show small cards of information. This might be our target abstraction.
  - Lack of "history" suppport (e.g. back button)

Action plan:

  - Prepare for a router to be placed in the system
  - Keep the path + time abstraction
  - Keep the category (Home, Views, Comments, Students, Answers) abstraction
  - Each category has a set of components, these should follow the following interface:
    + renderSummaryCard(path, period)            -- Can be skipped for now
    + renderDetailed(path, period, ownState)     -- Own state to support custom queries and the router
  - Category components should include:
    + A title
    + A description of what they do
    + If relevant a set of parameters, which allows us to tweak the query further
    + Be designed with a summary in mind
    + Category components should be independent from eachother
    + May delegate to sub-components (e.g. viewing all students -> details, or viewing all videos -> details)
      - This can take-over the entire view, but doesn't have to.
