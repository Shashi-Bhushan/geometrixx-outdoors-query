;
"use strict";
(function($){
  $(document).ready(function(){
    var source = $('#product-search-template').html();


    var compiled = dust.compile(source, 'product-search-template');
    dust.loadSource(compiled);

      $('input[name=productSearchSubmit]').click(function(){
        var textFieldValue = $('input[name=productSearch]').val();

        var data = {
                     "stats" : {
                       "startIndex" : 1,
                       "endIndex" : 2,
                       "totalItems" : 2
                     },
                     "results" : [
                       {
                         "url" : "/content/geometrixx-outdoors/en/men/shirts/ashanti-nomad.html",
                         "title" : "Ashanti Nomad",
                         "date" : "Jun 11, 2013",
                         "similar-url" : "/content/geometrixx-outdoors/en/toolbar/search.html?q=related%3A%2Fcontent%2Fgeometrixx-outdoors%2Fen%2Fmen%2Fshirts%2Fashanti-nomad"
                       },
                       {
                         "url" : "/content/geometrixx-outdoors/en/activities/nairobi-runners-running.html",
                         "title" : "Running",
                         "date" : "Jun 11, 2013",
                         "similar-url" : "/content/geometrixx-outdoors/en/toolbar/search.html?q=related%3A%2Fcontent%2Fgeometrixx-outdoors%2Fen%2Fmen%2Fshirts%2Fashanti-nomad"
                       }
                     ]
                   };
        $.ajax({
          url : '/bin/product-search.json',
          method : 'GET',
          dataType : 'json',
          data : {
            "searchTerm" : textFieldValue
          },
          success : function(response) {
            console.log(response);
            data.results = response;

            // Render the Data
            dust.render('product-search-template', data, function(err, out){
              $('#output').html(out);
            });
          }
        });
      });
  });

})(jQuery);
