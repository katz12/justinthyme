from django.shortcuts import render_to_response
from recipe_app.models import Recipe
from django.http import HttpResponse

from django.db import transaction, connection
import json

def test(request):
    return render_to_response('test.html')

def name_search(request):
    search = request.GET.get('search')
    results = Recipe.objects.raw('select * from recipe where name like %s',['%' + search + '%'])
    return render_to_response('test/name_search.html', {'results' : results})

def recipe_insert(request):
    name = request.GET.get('name')
    url = request.GET.get('url')
    cook_time = request.GET.get('cook_time')
    img_url = request.GET.get('img_url')

    cursor = connection.cursor()
    values = [name,url,cook_time,img_url]
    try:
        cursor.execute('insert into recipe (name,url,cook_time,img_url) values(%s,%s,%s,%s)', values)
    except Exception, e:
        print e
        return HttpResponse(status=500)
    transaction.commit_unless_managed()
    return HttpResponse()

# Derek add views here

# Katie add views here
def index(request):
    return render_to_response('index.html')

def contact(request):
    return render_to_response('site/ContactUs.html')

def about(request):
    return render_to_response('site/AboutUs.html')

def print_page(request):
    result = Recipe.objects.raw('select * from recipe, recipe_ingredient where recipe.id == 5 AND recipe.id == recipe_ingredient.recipe_id')
    return render_to_response('site/result.html', {'result': result})

# Michal add views here

# Andy add views here
def api_test(request):
    return HttpResponse(json.dumps({'test' : 'passed'}), mimetype="application/json")
