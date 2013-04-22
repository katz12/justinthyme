from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'justinthyme.views.home', name='home'),
    # url(r'^justinthyme/', include('justinthyme.foo.urls')),
    url(r'^test/$', 'recipe_app.views.test'),
    url(r'test/name_search/$', 'recipe_app.views.name_search'),
    url(r'test/recipe_insert/$', 'recipe_app.views.recipe_insert'),

    # Derek add URLs here

    # Kate add URLs here

    # Michal add URLs here

    # Andy add URLs here
    url(r'^api/test/$', 'recipe_app.views.api_test'),


    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
     url(r'^admin/', include(admin.site.urls)),
)
