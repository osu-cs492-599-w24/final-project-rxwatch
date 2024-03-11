# RxWatch
A simple Android application built for CS 492 at Oregon State University with Prof. Rob Hess, Winter 2024.

Built by:
- Aiden Freeman
- Josiah Humber
- Riley Rice
- Seth Weiss

## High-level overview
RxWatch focuses on providing users accessible information about drug interactions and adverse
drug effects for any medication that they may currently be using. The app provides a search box
where they can enter the name for the medication they are seeking information about, and to allows
them to fine tune those results based on other filters. 

The application has multiple screens. The main screen includes a search bar and button 
along with a display of the results after a search is initiated. The other screens contain
more in-depth information about the results of the search.

All the information that is displayed in the application is be taken from the
[OpenFDA API]([url](https://open.fda.gov/)) which is approved and updated by the FDA.

The OpenFDA API does not require a regsitered accont or key. 

## Third-party API calls
- Drug Labeling endpoint: https://open.fda.gov/apis/drug/label/ 
- Adverse Effects endpoint: https://open.fda.gov/apis/drug/event/ 
