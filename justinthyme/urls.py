from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'justinthyme.views.home', name='home'),
    # url(r'^justinthyme/', include('justinthyme.foo.urls')),
    url(r'site/search/$', 'recipe_app.views.test'),
    url(r'site/search/name_search/$', 'recipe_app.views.name_search'),
    url(r'site/test/recipe_insert/$', 'recipe_app.views.recipe_insert'),


    url(r'site/search/ingredient_search/$', 'recipe_app.views.ingredient_search'),


    # Derek add URLs here

    # Kate add URLs here
    url(r'^site/search/$', 'recipe_app.views.search', name='index'),
    url(r'site/contacts/$', 'recipe_app.views.contact', name='contact'),
    url(r'site/about/$', 'recipe_app.views.about', name='about'),
    url(r'site/search/result/$', 'recipe_app.views.print_page', name='print_page'),

    # Michal add URLs here
    #url(r'^search/$', 'recipe_app.views.search'),
    #url(r'search/recipe_search/$', 'recipe_app.views.recipe_search'),

    # Andy add URLs here
    url(r'^api/test/$', 'recipe_app.views.api_test'),
    url(r'^api/search/$', 'recipe_app.views.api_search'),
    url(r'^api/login/$', 'recipe_app.views.api_login'),
    url(r'^api/favorite/$', 'recipe_app.views.api_favorite'),
    url(r'^api/favorites/$', 'recipe_app.views.api_favorites'),
    url(r'^api/unfavorite/$', 'recipe_app.views.api_unfavorite'),

    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
     url(r'^admin/', include(admin.site.urls)),
)
