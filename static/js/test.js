$(document).ready(function(){
    
    $('#name_search input').keypress(function(key){
        
        // Enter Key
        if(key.which == 13){
            key.preventDefault();
            var search_text = $(this).val();
            $.ajax({url : 'name_search',
                    data : {search : search_text},
                    success : function(data){
                        $('#results').html(data).fadeIn(300);
                    },
                    error : function(data){
                        $('#results').html('There was an error.').fadeIn(300);
                    }
            });
        }
    });

    $('#submit').click(function(event){
        event.preventDefault();
        var name = $('#name').val();
        var url = $('#url').val();
        var cook_time = $('#cook_time').val();
        var img_url = $('#img_url').val();
        $.ajax({url : 'recipe_insert',
                data : {name : name, url : url, cook_time : cook_time, img_url : img_url},
                success : function(data){
                    $('#name').val('');
                    $('#url').val('');
                    $('#cook_time').val('');
                    $('#img_url').val('');
                }
        });
    });
});
