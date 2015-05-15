# aem-boilerplate
<p>This is our Adobe At Adobe AEM boilerplate for new projects.  This boilerplate builds on the maven multi module archetype and some of the published Adobe standards.  This is our boilerplate based on our preferences within our team. </p>

<p>This project is modeled off a project we are doing for the customer experience center.  I have left some code in the project to server as examples of what goes where.</p>

<h2>Project Structure</h2>
<ul>
    <li><b>Reactor</b>
        <p>this is where we control the build order</p>
        <ul>
            <li><b>modules</b>
                <ul>
                    <li><b>application</b>
                        <p>This module includes the bundle module and the application core logic but does not include the config or default site content when you do a maven install.  This is where most of our components live and our core design files.  This is the module that gets updated the most from release to release.  We separate it out the default content and the config because deploying those after users have been authoring on the platform can be dangerous.</p>
                    </li>
                    <li><b>bundle</b>
                        <p>Boilerplate Bundle where we include our servlets and all that fun stuff</p>
                    </li>
                    <li><b>config</b>
                        <p>The config module is where we put our OSGI configurations,ACL scripts and cloud configurations.  I left a few in there as examples.  The config module will typically only get deployed at the start of the project and rarely after that point.</p>
                    </li>
                    <li><b>site-content</b>
                        <p>This is where we keep our base site content.  This is typically never used after the initial deployment because it runs the risk of altering authored content.</p>
                    </li>
                </ul>
            </li>
            <li><b>parent</b>
                <p>The parent application is where we manage most of the data for building the project, the developer info, repos, properties and dependency management.  This project was modeled off a 6.1 application.  I left some examples in here for demonstration use and they might not fit your environment at all.</p>
            </li>
        </ul>
    </li>
</ul>

<h2>Use</h2>
<p>In each of the modules we define profiles for deploying the module.  They are all separate except the application does include the bundle by default.  This allows us to define what gets pushed to the server on a maven install.  So for example if we are working on just the application logic we don’t want to push the config or overwrite the default content.  We can control this by selecting which profiles get used when we do the maven install command.</p>
<h4>Example - install Config module from command line</h4>
```bash
    mvn install -P  autoInstallConfig
```

<h4>Example - install default content module from command line</h4>
```bash
    mvn install -P autoInstallDefaultContent
```

<h4>Example - install application module from command line</h4>
```bash
    mvn install -P autoInstallApplication
```

<h4>Example - install application module and default content from command line</h4>
```bash
    mvn install -P autoInstallApplication,autoInstallDefaultContent
```

<h2>Whats different from the maven multi module archetype</h2>
<ul>
    <li>We break out config, default content, bundle and application.  I found that I was having to do this on every project so it became our new standard</li>
    <li>Build profiles on each module so that we can tune what gets deployed or not deployed item by item</li>
    <li>We are putting application client libs under application.  Design is for design and its been getting too polluted with application logic lately.  We should be able to switch from design one to design two with a single content change, in theory.</li>
    <li>We break out templates, pages, and components into their own sub folders under the application.  Again it was getting too polluted when all mixed together</li>
    <li>On our example component you will see that we put the WMCUse java code in with the component.  Yes, this is against best practices and yes it makes code hinting less fun. The reason I like it inline is when there is an issue and we need to make a change to a single components logic we don't have to redeploy a bundle that may have all sorts of dependencies in other sections of the application.  Also if needed we can go into crxde and implement a quick work around for an major production issue. I will say that all common reused methods should be in a bundle and shared across the inline components.  This is my personal preference.</li>
    <li>I also include libs/foundation/global.jsp in the code base but don't include it in the package filter.  This is so your IDE can resolve global.jsp but you never want to really deploy it or change it.</li>
</ul>